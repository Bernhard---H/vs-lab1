package util;

import client.Client;
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

    public ClientResourceManager(Client client, ClientSessionManager sessionManager, InputStream userRequestStream, PrintStream userResponseStream) {
        super(sessionManager, userRequestStream, userResponseStream);
        assert client != null;
        this.client = client;
        this.clientSessionManager = sessionManager;
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

}
