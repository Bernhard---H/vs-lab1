package network.impl;

import network.NetClient;
import network.NetServer;
import network.NetworkException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class AbstractTcpServer implements NetServer {

    private static final Log logger = LogFactory.getLog(AbstractTcpServer.class);

    private ServerSocket serverSocket = null;

    public AbstractTcpServer(int port) throws NetworkException {
        try {
            this.serverSocket = new ServerSocket(port);
            // enables thread to be interrupted
            // 500ms ... half a second
            this.serverSocket.setSoTimeout(500);
        } catch (IOException e) {
            throw new TcpSetupException("creating the tcp socket failed", e);
        }
    }


    @Override
    public void run() {
        logger.info("start thread");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket client = this.serverSocket.accept();
                    NetClient netClient = new TcpClient(client);
                    this.handleNewConnection(netClient);
                } catch (SocketTimeoutException e) {
                    // ignore: check if thread is interrupted and try again
                }
            }
        } catch (IOException e) {
            logger.fatal("error while waiting for client connection", e);
        } catch (NetworkException e) {
            logger.fatal("Network exception in tcp server", e);
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
    }

    protected abstract void handleNewConnection(NetClient netClient) throws NetworkException;


    public void closeMe() {
        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                logger.error("io exception while closing", e);
            } finally {
                this.serverSocket = null;
            }
        }
    }

}
