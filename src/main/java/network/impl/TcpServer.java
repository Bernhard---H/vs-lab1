package network.impl;

import network.NetServer;
import network.NetworkException;
import network.impl.tcpStates.InitState;
import network.impl.tcpStates.State;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.CloseMe;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class TcpServer implements NetServer {

    private static final Log logger = LogFactory.getLog(TcpServer.class);

    private State currentState = new InitState();
    private ServerResourceManager rm;

    public TcpServer(ServerResourceManager rm) {
        this.rm = rm;
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

            while (!Thread.currentThread().isInterrupted() && this.currentState != null) {
                // run the action of the current state
                this.currentState = this.currentState.run(this.rm);
            }

        } catch (NetworkException e) {
            logger.fatal("Network exception in tcp server",e);
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
    }


    public void closeMe() {
        if (this.currentState != null) {
            this.currentState.closeMe();
            this.currentState = null;
        }
        if (this.rm != null) {
            CloseMe closeMe = this.rm;
            this.rm = null;
            closeMe.closeMe();
        }
    }

}
