package terminal.model;

import network.ConnectionPlus;
import network.model.Address;
import terminal.SessionState;
import util.CloseMe;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Bernhard Halbartschlager
 */
public final class Session implements CloseMe {

    private SessionState state = SessionState.NOBODY;
    private ConnectionPlus connection;
    private String user = null;
    private Address privateAddress = null;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public Session(ConnectionPlus connection) {
        assert connection != null;
        this.connection = connection;
    }

    public SessionState getState() {
        this.lock.readLock().lock();
        try {
            return this.state;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public String getName() {
        this.lock.readLock().lock();
        try {
            return this.user;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public ConnectionPlus getConnection() {
        ConnectionPlus connection = this.connection;
        if (connection == null){
            throw new IllegalStateException("session has been closed");
        }
        return connection;
    }

    public Address getPrivateAddress() {
        return privateAddress;
    }

    public void registerPrivateAddress(Address privateAddress) {
        assert privateAddress != null;
        this.privateAddress = privateAddress;
    }

    public void setAuthenticated(String user) {
        this.lock.writeLock().lock();
        try {
            this.state = SessionState.AUTHENTICATED;
            this.user = user;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void setLoggedOut() {
        this.lock.writeLock().lock();
        try {
            this.state = SessionState.NOBODY;
            this.user = null;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void closeMe() {
        this.setLoggedOut();
        if (this.connection != null) {
            this.connection.closeMe();
            this.connection = null;
        }
    }
}
