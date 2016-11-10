package util;

import chatserver.Chatserver;
import terminal.SessionManager;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerResourceManager extends ResourceManager {

    private Chatserver server;

    public ServerResourceManager(Chatserver server, SessionManager sessionManager, InputStream userRequestStream, PrintStream userResponseStream) {
        super(sessionManager, userRequestStream, userResponseStream);
        assert server != null;
        this.server = server;
    }

    public Chatserver getServer() {
        return server;
    }
}
