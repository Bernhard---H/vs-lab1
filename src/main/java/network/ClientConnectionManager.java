package network;

import util.CloseMe;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientConnectionManager implements CloseMe {

    private Connection server = null;

    private void createServerConnection() {

    }

    public Connection getServer() {
        if (this.server == null) {
            this.createServerConnection();
        }
        return server;
    }


    @Override
    public void closeMe() {
        if (this.server != null){
            this.server.closeMe();
            this.server = null;
        }
    }

}
