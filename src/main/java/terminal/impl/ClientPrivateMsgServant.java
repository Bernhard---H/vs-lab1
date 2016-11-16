package terminal.impl;

import network.ConnectionPlus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.Servant;
import terminal.ServantException;
import util.ClientResourceManager;
import util.CloseMe;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientPrivateMsgServant extends Servant<ClientResourceManager> {

    private static final Log logger = LogFactory.getLog(ClientPrivateMsgServant.class);

    private ConnectionPlus connection;

    public ClientPrivateMsgServant(ConnectionPlus connection, ClientResourceManager rm) throws ServantException {
        super(rm);
        assert connection != null;
        this.connection = connection;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        logger.info("start thread");

        try {
            String input = this.connection.read();
            this.println(input);
            this.connection.print("!ack");

        } catch (InterruptedException e) {
            // ingore and exit
            //logger.info("thread interrupted: closing");
        } catch (Exception e) {
            logger.fatal("random exception: ", e);
            // shutdown client
            this.rm.closeMe();
        } finally {
            this.closeMe();
        }

        logger.info("closing thread");
    }


    private void println(String msg) {
        assert this.rm != null;
        assert this.rm.getUserResponseStream() != null;
        this.rm.getUserResponseStream().println("\n" + msg);
    }


    @Override
    public void closeMe() {
        super.closeMe();
        if (this.connection != null) {
            CloseMe closeMe = this.connection;
            this.connection = null;
            closeMe.closeMe();
        }
    }

}
