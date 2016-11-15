package network.impl.tcpStates;

import network.NetClient;
import network.NetworkException;
import network.impl.TcpClient;
import network.impl.TcpClientConnectException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.ServerResourceManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author Bernhard Halbartschlager
 */
public final class WaitForClientState implements State {

    private static final Log logger = LogFactory.getLog(WaitForClientState.class);

    private ServerSocket serverSocket;

    public WaitForClientState(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    @Override
    public State run(ServerResourceManager rm) throws NetworkException {

        try {
            Socket client = this.serverSocket.accept();

            NetClient netClient = new TcpClient(client);
            rm.getConnectionManager().addNewConnection(netClient);

            return this;
        } catch (SocketTimeoutException e) {
            // ignore: check if thread is interrupted and try again
            return this;
        } catch (IOException e) {
            throw new TcpClientConnectException("error while waiting for client connection", e);
        }
    }

    @Override
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
