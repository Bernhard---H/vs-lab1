package network;

import network.impl.InnerServantException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.ServantException;
import terminal.SessionState;
import terminal.impl.ServerTcpServant;
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

    private List<Session> openSessions = new ArrayList<>();
    private ReadWriteLock sessionsLock = new ReentrantReadWriteLock();
    private ServerResourceManager rm;

    public ServerConnectionManager(ServerResourceManager rm) {
        assert rm != null;
        this.rm = rm;
    }

    /**
     * TcpServer just accepted the connection to a client
     *
     * @param client
     * @throws NetworkException
     */
    public void addNewConnection(NetClient client) throws NetworkException {
        assert client != null;
        this.sessionsLock.writeLock().lock();

        if (this.openSessions == null) {
            throw new IllegalStateException("SessionManager has already been closed");
        }
        assert this.rm != null;
        assert this.rm.getThreadManager() != null;
        this.rm.getThreadManager().execute(client);

        ConnectionContainer connection = this.wrapConnection(client);
        Session session = new Session(connection);
        this.openSessions.add(session);

        try {
            this.rm.getThreadManager().execute(new ServerTcpServant(session, this.rm));
        } catch (ServantException e) {
            throw new InnerServantException("failed to crate server tcp servant", e);
        }

        this.sessionsLock.writeLock().unlock();
    }

    private ConnectionContainer wrapConnection(ConnectionPlus client) {
        return new ConnectionContainer(client);
    }

    /**
     * to anyone who is willing to receive (except the sender)
     */
    public void broadcast(Session sender, String message) {
        this.sessionsLock.readLock().lock();
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
        this.sessionsLock.readLock().unlock();
    }

    /**
     * for logged in users only
     */
    public void authenticatedBroadcast(Session sender, String message) {
        this.sessionsLock.readLock().lock();
        if (this.openSessions == null) {
            throw new IllegalStateException("SessionManager has already been closed");
        }
        for (Session session : this.openSessions) {
            if (session != sender && session.getState() == SessionState.AUTHENTICATED) {
                ConnectionPlus connection = session.getConnection();
                try {
                    connection.print(message);
                } catch (IllegalStateException | NetworkException e) {
                    // ignore
                    logger.info(e);
                }
            }
        }
        this.sessionsLock.readLock().unlock();
    }

    public Session getSession(String name) {
        this.sessionsLock.readLock().lock();
        if (this.openSessions == null) {
            throw new IllegalStateException("SessionManager has already been closed");
        }
        for (Session session : this.openSessions) {
            if (session.getName().equals(name)) {
                this.sessionsLock.readLock().unlock();
                return session;
            }
        }
        this.sessionsLock.readLock().unlock();
        return null;
    }

    public List<String> getOnlineUsers() {
        this.sessionsLock.readLock().lock();
        if (this.openSessions == null) {
            throw new IllegalStateException("SessionManager has already been closed");
        }

        List<String> online = new ArrayList<>();
        for (Session session : this.openSessions) {
            ConnectionPlus connection = session.getConnection();
            if (connection != null && !connection.isClosed()) {
                String name = session.getName();
                if (name != null && !name.isEmpty()) {
                    online.add(name);
                }
            }
        }

        this.sessionsLock.readLock().unlock();
        return online;
    }


    @Override
    public void closeMe() {
        this.sessionsLock.writeLock().lock();
        if (this.openSessions != null) {
            Iterable<Session> sessions = this.openSessions;
            this.openSessions = null;
            for (CloseMe client : sessions) {
                client.closeMe();
            }
        }
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
            if (openSessions != null) {
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
            sessionsLock.writeLock().unlock();
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
