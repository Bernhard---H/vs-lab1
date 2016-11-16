package network.impl;

import network.NetClient;
import network.NetworkException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.CloseMe;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class TcpServer extends AbstractTcpServer {

    private static final Log logger = LogFactory.getLog(TcpServer.class);

    private ServerResourceManager rm;

    public TcpServer(int port, ServerResourceManager rm) throws NetworkException {
        super(port);
        assert rm != null;
        this.rm = rm;
    }

    @Override
    protected void handleNewConnection(NetClient netClient) throws NetworkException {
        this.rm.getConnectionManager().addNewConnection(netClient);
    }

    public void closeMe() {
        super.closeMe();
        if (this.rm != null) {
            CloseMe closeMe = this.rm;
            this.rm = null;
            closeMe.closeMe();
        }
    }

}
