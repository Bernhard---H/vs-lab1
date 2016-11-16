package util;

import chatserver.Chatserver;
import network.ServerConnectionManager;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerResourceManager extends ResourceManager {

    private Chatserver server;
    private ServerConnectionManager connectionManager;
    private Config users;

    public ServerResourceManager(Chatserver server, Config config, Config users, InputStream userRequestStream, PrintStream userResponseStream) {
        super(config, userRequestStream, userResponseStream);
        assert server != null;
        assert users != null;
        this.server = server;
        this.users = users;
        this.connectionManager = new ServerConnectionManager(this);
    }

    public Chatserver getServer() {
        return server;
    }

    public Config getUsers() {
        return users;
    }

    public ServerConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Waring: this will try to close everything
     */
    @Override
    public void closeMe() {
        super.closeMe();
        if (this.connectionManager != null) {
            CloseMe closeMe = this.connectionManager;
            this.connectionManager = null;
            closeMe.closeMe();
        }
    }
}
