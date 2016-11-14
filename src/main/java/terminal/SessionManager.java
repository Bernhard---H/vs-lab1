package terminal;

import network.NetworkException;
import terminal.model.Session;
import util.CloseMe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class SessionManager implements CloseMe {

    private List<Session> openSessions = new ArrayList<>();
    private boolean closed = false;


    public void broadcast(Session sender, String message) throws NetworkException {
        for (Session session : this.openSessions) {
            if (session != sender) {
                session.getConnection().print(message);
            }
        }
    }

    public void addSession(Session session) {
        if (this.closed) {
            throw new IllegalStateException("SessionManager has already been closed");
        }
        this.openSessions.add(session);
    }

    public Session getSession(String name) {
        for (Session session : this.openSessions) {
            if (session.getName().equals(name)) {
                return session;
            }
        }
        return null;
    }

    public void closeSession(String name) {
        if (this.closed) {
            throw new IllegalStateException("SessionManager has already been closed");
        }
        Session session = this.getSession(name);
        if (session != null) {
            session.closeMe();
        }
    }

    @Override
    public void closeMe() {
        this.closed = true;
        for (Session session : this.openSessions) {
            session.closeMe();
        }
        this.openSessions = new ArrayList<>();
    }
}
