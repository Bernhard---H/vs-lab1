package terminal.impl;

import network.ConnectionPlus;
import network.NetworkException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.Servant;
import terminal.ServantException;
import terminal.exceptions.ParseException;
import terminal.instruction.impl.*;
import terminal.model.Session;
import terminal.parser.impl.CommandParser;
import util.CloseMe;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerTcpServant extends Servant implements Runnable {

    private static final Log logger = LogFactory.getLog(ServerTcpServant.class);

    private Session session;


    public ServerTcpServant(Session session, ServerResourceManager rm) throws ServantException {
        super(rm);
        assert session != null;
        this.session = session;

        this.store = new InstructionStore();
        this.store.register(new SendTcpServerInstruction(rm, session));
        this.store.register(new LoginTcpServerInstruction(rm, session));
        this.store.register(new LogoutTcpServerInstruction(rm, session));
        this.store.register(new RegisterTcpServerInstruction(session));
        this.store.register(new LookupTcpServerInstruction(rm, session));
        //this.store.register(new ExitTcpServerInstruction(rm, session));
        this.parser = new CommandParser();
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
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Session session = this.session;
                    if (session == null){
                        throw new InterruptedException();
                    }
                    ConnectionPlus connection = session.getConnection();
                    if (connection == null){
                        throw new InterruptedException();
                    }
                    String line = connection.read();
                    this.println(this.runInput(line));
                } catch (ParseException e) {
                    printError(e);
                    logger.info(e);
                }
            }
        } catch (InterruptedException e) {
            // ingore and exit
        } catch (Exception e) {
            logger.fatal("random exception: ", e);
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
    }


    private void println(String msg) {
        try {
            // session might already be closed
            Session session = this.session;
            if (session != null) {
                ConnectionPlus connection = session.getConnection();
                if (connection != null) {
                    connection.print("!response: " + msg + "\n");
                }
            }
        } catch (NetworkException e) {
            // only relevant for udp connections
            assert false;
            logger.error(e);
        }
    }

    private void printError(String e) {
        this.println("ERROR: " + e);
    }

    private void printError(Exception e) {
        this.println("ERROR: " + e.getMessage());
        logger.error(e);
    }


    @Override
    public synchronized void closeMe() {
        // call to super would also close the server!
        // super.closeMe();
        if (this.session != null) {
            CloseMe closeMe = this.session;
            this.session = null;
            closeMe.closeMe();
        }
    }

}
