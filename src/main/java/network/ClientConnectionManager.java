package network;

import util.CloseMe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientConnectionManager implements CloseMe {

    private Connection server = null;
    private Lock serverLock = new ReentrantLock();

    private void createServerConnection() {

    }

    public Connection getServer() {
        this.serverLock.lock();
        if (this.server == null) {
            this.createServerConnection();
        }
        this.serverLock.unlock();
        return server;
    }


    @Override
    public synchronized void closeMe() {
        if (this.server != null){
            this.server.closeMe();
            this.server = null;
        }
    }

}
