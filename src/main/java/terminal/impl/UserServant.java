package terminal.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.Servant;
import terminal.exceptions.ParseException;
import terminal.instruction.IInstruction;
import terminal.instruction.IInstructionStore;
import terminal.model.Command;
import terminal.model.IArguments;
import terminal.parser.IArgumentsParser;
import terminal.parser.ICommandParser;
import terminal.parser.impl.CommandParser;
import util.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class UserServant<R extends ResourceManager> extends Servant<R> {

    protected IInstructionStore<R> store = null;
    private ICommandParser parser = null;
    protected String prompt;

    private static final Log logger = LogFactory.getLog(UserServant.class);

    public UserServant(R rm, String prompt) {
        super(rm);
        assert prompt != null;
        this.parser = new CommandParser();
        this.prompt = prompt;
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
        InputStream inputStream = this.rm.getUserRequestStream();

        try (Scanner scanner = new Scanner(inputStream)) {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    this.printPrompt();
                    String line = this.interruptableReadline(inputStream, scanner);
                    this.runInput(line);
                }
            } catch (InterruptedException e) {
                // ingore and exit
            }
        } catch (IOException e) {
            this.printError(e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
    }

    private String interruptableReadline(InputStream inputStream, Scanner scanner) throws InterruptedException, IOException {
        while (!Thread.currentThread().isInterrupted()) {
            if (inputStream.available() > 0) {
                // can read something
                try {
                    // read line
                    return scanner.nextLine();
                } catch (NoSuchElementException e) {
                    // not a full line jet
                    Thread.sleep(100);
                }
            } else {
                Thread.sleep(100);
            }
        }
        throw new InterruptedException();
    }

    protected void printPrompt() {
        this.rm.getUserResponseStream().print(this.prompt + "> ");
    }

    protected void println(String msg) {
        this.rm.getUserResponseStream().println(msg);
    }

    protected void printError(String e) {
        this.rm.getUserResponseStream().println("ERROR: " + e);
    }

    protected void printError(Exception e) {
        this.rm.getUserResponseStream().println("ERROR: " + e.getMessage());
    }

    protected <T extends IArguments> void runInput(String input) {
        assert input != null;

        try {
            Command command = this.parser.parse(input);
            IInstruction<T, R> instruction = this.store.findInstruction(command);
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
            this.printError(e);
        }
    }

    @Override
    public void closeMe() {
        super.closeMe();
    }

}
