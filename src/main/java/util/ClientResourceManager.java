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

    private Client client;
    private String lastPublicMessage = null;
    private ClientSessionManager clientSessionManager;
    private Config config;
    private ClientConnectionManager connectionManager = new ClientConnectionManager(this);

    public ClientResourceManager(Client client, ClientSessionManager sessionManager, Config config, InputStream userRequestStream, PrintStream userResponseStream) {
        super(sessionManager, userRequestStream, userResponseStream);
        assert client != null;
        assert config != null;
        this.client = client;
        this.clientSessionManager = sessionManager;
        this.config = config;
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

    public Config getConfig() {
        return config;
    }
}
