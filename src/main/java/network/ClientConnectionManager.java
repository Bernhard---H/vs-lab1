package network;

import network.impl.TcpClient;
import network.model.Address;
import util.ClientResourceManager;
import util.CloseMe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientConnectionManager implements CloseMe {

    private Connection toServer = null;
    private Lock serverLock = new ReentrantLock();
    private ClientResourceManager rm;

    public ClientConnectionManager(ClientResourceManager rm) {
        this.rm = rm;
    }

    private void createServerConnection() throws NetworkException {
        String host = this.rm.getConfig().getString("chatserver.host");
        int port = this.rm.getConfig().getInt("chatserver.tcp.port");
        TcpClient client = new TcpClient(new Address(host, port));
        this.rm.getThreadManager().execute(client);
        this.toServer = client;
    }

    public Connection getServer() throws NetworkException {
        this.serverLock.lock();
        if (this.toServer == null) {
            this.createServerConnection();
        }
        this.serverLock.unlock();
        return toServer;
    }

    @Override
    public void closeMe() {
        this.serverLock.lock();
        if (this.toServer != null) {
            this.toServer.closeMe();
            this.toServer = null;
        }
        this.serverLock.unlock();
    }

}
