package network.impl.tcpStates;

import network.NetworkException;
import network.impl.InnerServantException;
import network.impl.TcpClientConnectException;
import terminal.ServantException;
import terminal.impl.ServerTcpServant;
import util.ResourceManager;

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
    public State run(ResourceManager rm) throws NetworkException {

        try {
            Thread.sleep(10000);
            Socket client = this.serverSocket.accept();
            // todo: do something with the connection

            ServerTcpServant servant = new ServerTcpServant(client);

            rm.getSessionManager()


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
        } catch (ServantException e) {
            throw new InnerServantException("error within the servant", e);
        }
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
