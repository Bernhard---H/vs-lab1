package util;

import chatserver.Chatserver;
import network.ServerConnectionManager;
import terminal.SessionManager;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerResourceManager extends ResourceManager {

    private Chatserver server;
    private ServerConnectionManager connectionManager;

    public ServerResourceManager(Chatserver server, SessionManager sessionManager, InputStream userRequestStream, PrintStream userResponseStream) {
        super(sessionManager, userRequestStream, userResponseStream);
        assert server != null;
        this.server = server;
    }

    public Chatserver getServer() {
        return server;
    }

    public ServerConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Waring: this will try to close everything
     */
    @Override
    public void closeMe() {
        this.closeMeLock.lock();
        super.closeMe();
        if (this.connectionManager != null) {
            CloseMe closeMe = this.connectionManager;
            this.connectionManager = null;
            closeMe.closeMe();
        }
        this.closeMeLock.unlock();
    }
}
