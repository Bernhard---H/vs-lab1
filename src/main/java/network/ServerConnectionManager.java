package network;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.model.Session;
import util.BlockingQueueTimeoutException;
import util.CloseMe;
import util.ServerResourceManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerConnectionManager implements CloseMe {

    private static final Log logger = LogFactory.getLog(ServerConnectionManager.class);

    /**
     * @deprecated
     */
    private List<ConnectionPlus> clientConnections = new ArrayList<>();
    private List<Session> openSessions = new ArrayList<>();
    private ReadWriteLock sessionsLock = new ReentrantReadWriteLock();
    private ServerResourceManager rm;

    public ServerConnectionManager(ServerResourceManager rm) {
        this.rm = rm;
    }

    /**
     * TcpServer just accepted the connection to a client
     *
     * @param client
     * @throws NetworkException
     */
    public void addNewConnection(NetClient client) throws NetworkException {
        this.sessionsLock.writeLock().lock();

        if (this.openSessions == null) {
            throw new IllegalStateException("SessionManager has already been closed");
        }
        this.rm.getThreadManager().execute(client);
        ConnectionContainer connection = this.wrapConnection(client);
        this.openSessions.add(new Session(connection));

        this.sessionsLock.writeLock().unlock();
    }

    private ConnectionContainer wrapConnection(ConnectionPlus client) {
        return new ConnectionContainer(client);
    }


    public void broadcast(Session sender, String message) {
        if (this.openSessions == null) {
            throw new IllegalStateException("SessionManager has already been closed");
        }
        for (Session session : this.openSessions) {
            if (session != sender) {
                ConnectionPlus connection = session.getConnection();
                try {
                    connection.print(message);
                } catch (IllegalStateException | NetworkException e) {
                    // ignore
                    logger.info(e);
                }
            }
        }
    }


    public Session getSession(String name) {
        if (this.openSessions == null) {
            throw new IllegalStateException("SessionManager has already been closed");
        }
        for (Session session : this.openSessions) {
            if (session.getName().equals(name)) {
                return session;
            }
        }
        return null;
    }


    @Override
    public void closeMe() {
        this.sessionsLock.writeLock().lock();
        for (CloseMe client : this.openSessions) {
            client.closeMe();
        }
        this.openSessions = null;
        this.sessionsLock.writeLock().unlock();

        if (this.rm != null) {
            CloseMe closeMe = this.rm;
            this.rm = null;
            closeMe.closeMe();
        }
    }

    /**
     * detect closed connections and remove them from the list
     */
    private class ConnectionContainer implements ConnectionPlus {

        private ConnectionPlus client;

        public ConnectionContainer(ConnectionPlus client) {
            this.client = client;
        }

        private void removeMeFromConnectionList() {
            sessionsLock.writeLock().lock();
            Iterator<Session> iterator = openSessions.iterator();
            while (iterator.hasNext()) {
                Session session = iterator.next();
                if (session.getConnection() == this) {
                    iterator.remove();
                    sessionsLock.writeLock().unlock();
                    return;
                }
            }
        }

        @Override
        public void closeMe() {
            client.closeMe();
            this.removeMeFromConnectionList();
        }

        @Override
        public void print(String msg) throws NetworkException {
            try {
                client.print(msg);
            } catch (IllegalStateException e) {
                this.removeMeFromConnectionList();
                throw e;
            }
        }

        public boolean isClosed() {
            boolean closed = client.isClosed();
            if (closed) {
                this.removeMeFromConnectionList();
            }
            return closed;
        }

        @Override
        public String read() throws InterruptedException {
            try {
                return client.read();
            } catch (IllegalStateException e) {
                this.removeMeFromConnectionList();
                throw e;
            }
        }

        @Override
        public String read(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException {
            try {
                return client.read(timeout, unit);
            } catch (IllegalStateException e) {
                this.removeMeFromConnectionList();
                throw e;
            }
        }
    }

}
