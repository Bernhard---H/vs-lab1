package network.impl;

import network.Connection;
import network.NetworkException;
import network.model.Address;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.CloseableBlockingQueue;
import util.IO;
import util.impl.MyCloseableBlockingQueue;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Bernhard Halbartschlager
 */
public final class TcpClient implements Connection, Runnable {

    private static final Log logger = LogFactory.getLog(TcpClient.class);

    private Socket socket = null;
    private PrintWriter out;
    private InputStream inputStream;
    private Scanner inputScanner;
    private CloseableBlockingQueue<String> msgQueue = new MyCloseableBlockingQueue<>();

    public TcpClient(Address address) throws NetworkException {
        assert address != null;
        try {
            this.socket = new Socket(address.getHost(), address.getPort());
        } catch (IOException e) {
            throw new TcpClientException("failed to create socket", e);
        }
    }

    @Override
    public void print(String msg) {
        if (this.out == null) {
            throw new IllegalStateException("Connection has not been started jet or has been closed");
        }
        this.out.println(msg);
    }

    @Override
    public String read() throws InterruptedException {
        return this.msgQueue.take();
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

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String line = IO.interruptableReadln(this.inputStream, this.inputScanner);
                    this.msgQueue.put(line);
                }
            } catch (InterruptedException e) {
                // ingore and exit
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
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
