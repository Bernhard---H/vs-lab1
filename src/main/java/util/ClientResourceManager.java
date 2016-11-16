package util;

import client.Client;
import network.ClientConnectionManager;
import terminal.impl.ClientSessionManager;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientResourceManager extends ResourceManager {

    private ClientSessionManager sessionManager;
    private Client client;
    private String lastPublicMessage = null;
    private ClientConnectionManager connectionManager = new ClientConnectionManager(this);

    public ClientResourceManager(Client client, ClientSessionManager sessionManager, Config config, InputStream userRequestStream, PrintStream userResponseStream) {
        super(config, userRequestStream, userResponseStream);

        assert sessionManager != null;
        assert client != null;
        this.sessionManager = sessionManager;
        this.client = client;
    }

    public ClientSessionManager getSessionManager() {
        return this.sessionManager;
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


    public ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }


    /**
     * Waring: this will try to close everything
     */
    @Override
    public void closeMe() {
        super.closeMe();

        this.connectionManager.closeMe();
    }
}
