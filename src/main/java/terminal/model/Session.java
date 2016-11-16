package terminal.model;

import network.ConnectionPlus;
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
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public Session(ConnectionPlus connection) {
        assert connection != null;
        this.connection = connection;
    }

    public SessionState getState() {
        this.lock.readLock().lock();
        SessionState tmp = this.state;
        this.lock.readLock().unlock();
        return tmp;
    }

    public String getName() {
        this.lock.readLock().lock();
        String tmp = this.user;
        this.lock.readLock().unlock();
        return tmp;
    }

    public ConnectionPlus getConnection() {
        return this.connection;
    }

    public void setAuthenticated(String user) {
        this.lock.writeLock().lock();
        this.state = SessionState.AUTHENTICATED;
        this.user = user;
        this.lock.writeLock().unlock();
    }

    public void setLoggedOut() {
        this.lock.writeLock().lock();
        this.state = SessionState.NOBODY;
        this.user = null;
        this.lock.writeLock().unlock();
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
