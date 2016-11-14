package util;

import client.Client;
import network.ClientConnectionManager;
import terminal.SessionManager;
import terminal.impl.ClientSessionManager;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientResourceManager extends ResourceManager {

    private SessionManager sessionManager;
    private Client client;
    private String lastPublicMessage = null;
    private ClientSessionManager clientSessionManager;
    private ClientConnectionManager connectionManager = new ClientConnectionManager(this);

    public ClientResourceManager(Client client, ClientSessionManager sessionManager, Config config, InputStream userRequestStream, PrintStream userResponseStream) {
        super(config, userRequestStream, userResponseStream);

        assert sessionManager != null;
        assert client != null;
        this.sessionManager = sessionManager;
        this.client = client;
        this.clientSessionManager = sessionManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public Client getClient() {
        return client;
    }

    public String getLastPublicMessage() {
        return lastPublicMessage;
    }

    public void setLastPublicMessage(String lastPublicMessage) {
        this.lastPublicMessage = lastPublicMessage;
    }

    public ClientSessionManager getClientSessionManager() {
        return clientSessionManager;
    }

    public ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }


    /**
     * Waring: this will try to close everything
     */
    @Override
    public void closeMe() {
        this.closeMeLock.lock();
        super.closeMe();

        this.sessionManager.closeMe();
        this.connectionManager.closeMe();
        this.closeMeLock.unlock();
    }
}
