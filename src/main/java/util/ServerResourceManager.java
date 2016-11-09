package util;

import chatserver.Chatserver;
import terminal.SessionManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerResourceManager extends ResourceManager {

    private Chatserver server;

    public ServerResourceManager(Chatserver server, SessionManager sessionManager) {
        super(sessionManager);
        assert server != null;
        this.server = server;
    }

    public Chatserver getServer() {
        return server;
    }
}
