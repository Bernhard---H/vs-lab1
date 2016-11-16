package network.impl;

import network.NetClient;
import network.NetworkException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.ServantException;
import terminal.impl.ClientPrivateMsgServant;
import util.ClientResourceManager;
import util.CloseMe;

/**
 * @author Bernhard Halbartschlager
 */
public final class PrivateTcpServer extends AbstractTcpServer {

    private static final Log logger = LogFactory.getLog(PrivateTcpServer.class);

    private ClientResourceManager rm;

    public PrivateTcpServer(int port, ClientResourceManager rm) throws NetworkException {
        super(port);
        assert rm != null;
        this.rm = rm;
    }

    @Override
    protected void handleNewConnection(NetClient netClient) throws NetworkException {
        this.rm.getThreadManager().execute(netClient);
        try {
            this.rm.getThreadManager().execute(new ClientPrivateMsgServant(netClient, this.rm));
        } catch (ServantException e) {
            throw new InnerServantException("failed to crate private message listener", e);
        }
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
