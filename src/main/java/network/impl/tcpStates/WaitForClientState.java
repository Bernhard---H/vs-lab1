package network.impl.tcpStates;

import network.NetClient;
import network.NetworkException;
import network.impl.TcpClient;
import network.impl.TcpClientConnectException;
import util.ServerResourceManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author Bernhard Halbartschlager
 */
public final class WaitForClientState implements State {

    private ServerSocket serverSocket;

    public WaitForClientState(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    @Override
    public State run(ServerResourceManager rm) throws NetworkException {

        try {
            Thread.sleep(10000);
            Socket client = this.serverSocket.accept();
            // todo: do something with the connection

            NetClient netClient = new TcpClient(client);

            rm.getConnectionManager().addNewConnection(netClient);

            return this;
        } catch (SocketTimeoutException e) {
            // ignore: check if thread is interrupted and try again
            return this;
        } catch (IOException e) {
            throw new TcpClientConnectException("error while waiting for client connection", e);
        } catch (InterruptedException e) {
            // close thread as soon as possible
            this.close();
            return null;
        }
        //catch (ServantException e) {
        //    throw new InnerServantException("error within the servant", e);
        //}
    }

    /**
     * must be callable multiple times and must not throw exceptions
     */
    @Override
    public void close() {
        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.serverSocket = null;
            }
        }
    }

}
