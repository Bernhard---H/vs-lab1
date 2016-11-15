package terminal.impl;

import network.ConnectionPlus;
import network.NetworkException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.Servant;
import terminal.ServantException;
import terminal.exceptions.ParseException;
import terminal.instruction.IInstruction;
import terminal.instruction.IInstructionStore;
import terminal.instruction.impl.InstructionStore;
import terminal.instruction.impl.SendClientInterceptInstruction;
import terminal.model.Command;
import terminal.model.IArguments;
import terminal.parser.IArgumentsParser;
import terminal.parser.ICommandParser;
import terminal.parser.impl.CommandParser;
import util.BlockingQueueTimeoutException;
import util.ClientResourceManager;
import util.CloseMe;
import util.CloseableBlockingQueue;
import util.impl.MyCloseableBlockingQueue;

import java.util.concurrent.TimeUnit;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientInterceptServant extends Servant<ClientResourceManager> implements ConnectionPlus {

    private static final Log logger = LogFactory.getLog(ServerTcpServant.class);

    private IInstructionStore<ClientResourceManager> store;
    private ICommandParser parser;
    private ConnectionPlus connection;
    private CloseableBlockingQueue<String> msgQueue = new MyCloseableBlockingQueue<>();

    public ClientInterceptServant(ConnectionPlus connection , ClientResourceManager rm) throws ServantException {
        super(rm);
        assert connection != null;
        this.connection = connection;

        this.store = new InstructionStore<>();
        this.store.register(new SendClientInterceptInstruction());
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
                    this.runInput(this.connection.read());
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


    private void println(String msg) {
        this.rm.getUserResponseStream().println(msg);
    }

    private void printError(String e) {
        this.println("ERROR: " + e);
    }

    private void printError(Exception e) {
        this.println("ERROR: " + e.getMessage());
        logger.error(e);
    }

    private  <T extends IArguments> void runInput(String input) throws InterruptedException {
        assert input != null;

        try {
            Command command = this.parser.parse(input);
            IInstruction<T, ClientResourceManager> instruction = this.store.findInstruction(command);
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
            }

            String result = instruction.execute(arguments, this.rm);
            this.println(result);
        } catch (ParseException e) {
            // let something or someone else handle the msg
            this.msgQueue.put(input);
        }
    }

    @Override
    public void print(String msg) throws NetworkException {
        this.connection.print(msg);
    }

    @Override
    public String read() throws InterruptedException {
        return this.msgQueue.take();
    }

    @Override
    public String read(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException {
        return this.msgQueue.poll(timeout, unit);
    }

    @Override
    public boolean isClosed() {
        return this.connection.isClosed();
    }

    @Override
    public synchronized void closeMe() {
        super.closeMe();
        if (this.connection != null) {
            CloseMe closeMe = this.connection;
            this.connection = null;
            closeMe.closeMe();
        }
        this.msgQueue.closeMe();
    }

}
