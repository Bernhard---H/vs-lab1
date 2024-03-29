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
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * throws IllegalStateException if TcpClient.closeMe() has been called
 *
 * @author Bernhard Halbartschlager
 */
public final class TcpClient implements NetClient {

    private static final Log logger = LogFactory.getLog(TcpClient.class);

    private Socket socket;
    private PrintWriter out;
    private Scanner inputScanner;
    private CloseableBlockingQueue<String> msgQueue = new MyCloseableBlockingQueue<>();
    private Semaphore bootSemaphore = new Semaphore(0);

    /**
     * construct actual tcp client for Client.java
     *
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
     *
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
            this.inputScanner = IO.toScanner(this.socket.getInputStream());

            // END of setup
            this.bootSemaphore.release(Integer.MAX_VALUE);

            while (!Thread.currentThread().isInterrupted()) {
                String line = IO.interruptableReadln(this.socket.getInputStream(), this.inputScanner);
                if (!line.trim().isEmpty()) {
                    // ignore empty lines
                    this.msgQueue.put(line);
                }
            }
        } catch (InterruptedException e) {
            // thread manager tells us to exit
        } catch (IOException e) {
            // ignore and exit
        } catch (Exception e) {
            logger.fatal("random Exception", e);
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
        if (this.inputScanner != null) {
            this.inputScanner.close();
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
