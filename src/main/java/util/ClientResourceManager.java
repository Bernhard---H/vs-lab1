package util;

import client.Client;
import terminal.SessionManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientResourceManager extends ResourceManager {

    private Client client;
    private String lastPublicMessage = null;

    public ClientResourceManager(Client client, SessionManager sessionManager) {
        super(sessionManager);
        assert client != null;
        this.client = client;
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

}
