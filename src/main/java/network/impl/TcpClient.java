package network.impl;

import network.NetClient;
import network.NetworkException;
import network.model.Address;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.BlockingQueueTimeoutException;
import util.CloseableBlockingQueue;
import util.IO;
import util.impl.MyCloseableBlockingQueue;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Bernhard Halbartschlager
 *
 * throws IllegalStateException if TcpClient.closeMe() has been called
 */
public final class TcpClient implements NetClient {

    private static final Log logger = LogFactory.getLog(TcpClient.class);

    private Socket socket;
    private PrintWriter out;
    private InputStream inputStream;
    private Scanner inputScanner;
    private CloseableBlockingQueue<String> msgQueue = new MyCloseableBlockingQueue<>();
    private Semaphore bootSemaphore = new Semaphore(0);

    /**
     * construct actual tcp client for Client.java
     * @param address
     * @throws NetworkException
     */
    public TcpClient(Address address) throws NetworkException {
        assert address != null;
        try {
            this.socket = new Socket(address.getHost(), address.getPort());
        } catch (IOException e) {
            throw new TcpClientException("failed to create socket", e);
        }
    }

    /**
     * misuse for tcp server after connection has been established
     * @param socket
     */
    public TcpClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void print(String msg) {
        try {
            // wait for TcpClient to finish booting
            this.bootSemaphore.acquire();
            this.bootSemaphore.release();
        } catch (InterruptedException e) {
            logger.debug(e);
            return;
        }
        if (this.out == null) {
            throw new IllegalStateException("Connection has already been closed");
        }
        this.out.println(msg);
    }

    @Override
    public String read() throws InterruptedException {
        return this.msgQueue.take();
    }

    @Override
    public String read(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException {
        return this.msgQueue.poll(timeout, unit);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        logger.info("start thread");
        try {
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.inputStream = this.socket.getInputStream();
            this.inputScanner = IO.toScanner(this.inputStream);

            // END of setup
            this.bootSemaphore.release(Integer.MAX_VALUE);

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String line = IO.interruptableReadln(this.inputStream, this.inputScanner);
                    this.msgQueue.put(line);
                }
            } catch (InterruptedException e) {
                // ingore and exit
            }
        } catch (IOException e) {
            logger.error(e);
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
    }

    @Override
    public synchronized boolean isClosed() {
        return this.socket == null;
    }

    @Override
    public synchronized void closeMe() {
        if (this.out != null) {
            this.out.close();
            this.out = null;
        }
        if (this.inputStream != null) {
            if (this.inputScanner == null) {
                // no scanner created jet
                try {
                    this.inputStream.close();
                } catch (IOException e) {
                    logger.info("input stream exception while closing", e);
                }
            } else {
                // scanner will autoclose stream
                this.inputScanner.close();
            }
            this.inputStream = null;
            this.inputScanner = null;
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                logger.info("exception from socket while closing: ", e);
            }
            this.socket = null;
        }
        this.msgQueue.closeMe();
    }


}
