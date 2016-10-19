package network.impl;

import network.NetServer;
import util.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.MissingResourceException;

/**
 * @author Bernhard Halbartschlager
 */
public final class TcpServer implements NetServer {

    /**
     * cache config for all instances of the tcp server
     */
    private static Config config = null;

    private int tcpPort;

    private ServerSocket serverSocket;

    public TcpServer() {
        this.loadConfig();
    }

    private void setup() {

        try {
            this.serverSocket = new ServerSocket(this.tcpPort);
            // enables thread to be interrupted
            // 500ms ... half a second
            this.serverSocket.setSoTimeout(500);
        } catch (IOException e) {
            // todo
            e.printStackTrace();
        }
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
        try {
            this.setup();

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    Socket client = this.serverSocket.accept();
                    // todo: do something with the connection

                } catch (SocketTimeoutException e) {
                    // ignore: check if thread is interrupted and try again
                }
            }

        } catch (InterruptedException e) {
            // ignore
        } catch (Exception e) {
            // make sure exception doesn't get swallowed
            e.printStackTrace();
        } finally {
            this._close();
        }
    }

    private void loadConfig() {
        try {
            if (config == null) {
                // load
                config = new Config("chatserver.properties");
            }

            this.tcpPort = config.getInt("tcp.port");
        } catch (MissingResourceException e) {
            // todo: new Config() failed   ||  could not find "tcp.port"
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // todo: failed to read port
            e.printStackTrace();
        }
    }


    @Override
    public void close() throws Exception {
        this._close();
    }

    /**
     * idempotent version of this.close()
     */
    private void _close() {
        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.serverSocket = null;
        }
    }

}
