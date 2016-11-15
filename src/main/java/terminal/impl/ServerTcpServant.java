package terminal.impl;

import network.NetworkException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.Servant;
import terminal.ServantException;
import terminal.exceptions.ParseException;
import terminal.instruction.IInstruction;
import terminal.instruction.IInstructionStore;
import terminal.instruction.impl.InstructionStore;
import terminal.instruction.impl.SendTcpServerInstruction;
import terminal.model.Command;
import terminal.model.IArguments;
import terminal.model.IServerArguments;
import terminal.model.Session;
import terminal.parser.IArgumentsParser;
import terminal.parser.ICommandParser;
import terminal.parser.impl.CommandParser;
import util.CloseMe;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerTcpServant extends Servant<ServerResourceManager> implements Runnable {

    private static final Log logger = LogFactory.getLog(UserServant.class);

    private IInstructionStore<ServerResourceManager> store;
    private ICommandParser parser;
    private Session session;


    public ServerTcpServant(Session session, ServerResourceManager rm) throws ServantException {
        super(rm);
        assert session != null;
        this.session = session;

        this.store = new InstructionStore<>();
        this.store.register(new SendTcpServerInstruction());
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
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    this.runInput(this.session.getConnection().read());
                }
            } catch (InterruptedException e) {
                // ingore and exit
            }
        } catch (Exception e) {
            // ignore
            logger.info(e);
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
    }


    protected void println(String msg) {
        try {
            this.session.getConnection().print(msg + "\n");
        }
        catch (NetworkException e) {
            // only relevant for udp connections
            assert false;
            logger.error(e);
        }
    }

    protected void printError(String e) {
        this.println("ERROR: " + e);
    }

    protected void printError(Exception e) {
        this.println("ERROR: " + e.getMessage());
        logger.error(e);
    }

    protected <T extends IArguments> void runInput(String input) {
        assert input != null;

        try {
            Command command = this.parser.parse(input);
            IInstruction<T, ServerResourceManager> instruction = this.store.findInstruction(command);
            if (instruction == null) {
                this.printError("no command '" + command.getName() + "' found");
                return;
            }

            IArgumentsParser<T> argsParser = instruction.getArgumentsParser();
            T arguments;
            if (argsParser == null) {
                arguments = null;
            } else {
                arguments = argsParser.parse(command.getParameter());
                if (arguments instanceof IServerArguments) {
                    ((IServerArguments) arguments).setSession(session);
                }
            }

            String result = instruction.execute(arguments, this.rm);
            this.println(result);
        } catch (ParseException e) {
            this.printError(e);
        }
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
