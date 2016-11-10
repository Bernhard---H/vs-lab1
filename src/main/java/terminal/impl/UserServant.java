package terminal.impl;

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

/**
 * @author Bernhard Halbartschlager
 */
public abstract class UserServant<R extends ResourceManager> extends Servant<R> {

    protected IInstructionStore<R> store = null;
    private ICommandParser parser = null;
    protected R rm = null;
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
        // todo: implement
    }

    protected void printPrompt() {
        this.rm.getUserResponseStream().print(this.prompt + "> ");
    }

    protected void println(String msg) {
        this.rm.getUserResponseStream().println(msg);
    }

    protected <T extends IArguments> void runInput(String input) throws ParseException {
        assert input != null;

        Command command = this.parser.parse(input);
        IInstruction<T, R> instruction = this.store.findInstruction(command);
        if (instruction == null) {
            // todo: throw exception
            return;
        }

        IArgumentsParser<T> argsParser = instruction.getArgumentsParser();
        T arguments = argsParser.parse(command.getParameter());

        String result = instruction.execute(arguments, this.rm);
        this.println(result);

    }

    @Override
    public void closeMe() {
        super.closeMe();
    }

}
