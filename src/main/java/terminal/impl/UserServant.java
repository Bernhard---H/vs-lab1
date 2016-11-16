package terminal.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.Servant;
import terminal.exceptions.ParseException;
import terminal.parser.impl.CommandParser;
import util.IO;
import util.ResourceManager;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class UserServant<R extends ResourceManager> extends Servant<R> {

    private static final Log logger = LogFactory.getLog(UserServant.class);

    protected String prompt;

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

        try (Scanner scanner = IO.toScanner(this.rm.getUserRequestStream())) {
            while (!Thread.currentThread().isInterrupted()) {
                this.printPrompt();
                String line = IO.interruptableReadln(this.rm.getUserRequestStream(), scanner);
                if (!line.trim().isEmpty()) {
                    try {
                        String result = this.runInput(line);
                        this.println(result);
                    } catch (ParseException e) {
                        this.printError(e);
                        logger.error(e);
                    }
                }
            }
        } catch (InterruptedException e) {
            // ingore and exit
            logger.info("thread interrupted: closing");
        } catch (IOException e) {
            this.printError(e);
        } catch (Exception e) {
            logger.fatal(e);
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
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
        logger.error(e);
    }

}
