package network;

import network.impl.InnerServantException;
import network.impl.TcpClient;
import network.impl.UdpClient;
import network.model.Address;
import terminal.ServantException;
import terminal.impl.ClientInterceptServant;
import util.ClientResourceManager;
import util.CloseMe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientConnectionManager implements CloseMe {

    private ConnectionPlus toServerTcp = null;
    private NetClient toServerUdp = null;
    private Lock serverLock = new ReentrantLock();
    private ClientResourceManager rm;

    public ClientConnectionManager(ClientResourceManager rm) {
        this.rm = rm;
    }

    private void createTcpConnection() throws NetworkException {
        String host = this.rm.getConfig().getString("chatserver.host");
        int port = this.rm.getConfig().getInt("chatserver.tcp.port");
        NetClient tcpClient = new TcpClient(new Address(host, port));
        this.rm.getThreadManager().execute(tcpClient);

        // wrap connection in interceptors:
        try {
            ClientInterceptServant servant = new ClientInterceptServant(tcpClient, this.rm);
            this.rm.getThreadManager().execute(servant);
            this.toServerTcp = new SyncResponseFilter(servant, this.rm);
        } catch (ServantException e) {
            throw new InnerServantException("failed to package server connection",e);
        }
    }

    public ConnectionPlus getTcpConnection() throws NetworkException {
        this.serverLock.lock();
        if (this.toServerTcp == null || this.toServerTcp.isClosed()) {
            this.createTcpConnection();
        }
        this.serverLock.unlock();
        return toServerTcp;
    }

    private void createUdpConnection() throws NetworkException {
        String host = this.rm.getConfig().getString("chatserver.host");
        int port = this.rm.getConfig().getInt("chatserver.udp.port");
        this.toServerUdp = new UdpClient(new Address(host, port));
        this.rm.getThreadManager().execute(this.toServerUdp);
    }

    public ConnectionPlus getUdpConnection() throws NetworkException {
        this.serverLock.lock();
        if (this.toServerUdp == null || this.toServerUdp.isClosed()) {
            this.createUdpConnection();
        }
        this.serverLock.unlock();
        return this.toServerUdp;
    }

    @Override
    public void closeMe() {
        this.serverLock.lock();
        if (this.toServerTcp != null) {
            this.toServerTcp.closeMe();
            this.toServerTcp = null;
        }
        if (this.toServerUdp != null) {
            this.toServerUdp.closeMe();
            this.toServerUdp = null;
        }
        this.serverLock.unlock();
    }

}
