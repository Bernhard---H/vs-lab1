package terminal;

import terminal.instruction.IInstruction;
import terminal.instruction.IInstructionStore;
import terminal.model.Command;
import terminal.model.IArguments;
import terminal.parser.IArgumentsParser;
import terminal.parser.ICommandParser;
import terminal.exceptions.ParseException;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class Servant {


    private IInstructionStore store = null;
    private ICommandParser parser = null;


    public abstract void println(String msg);


    protected <T extends IArguments> void runInput(String input) throws ParseException {
        assert input != null;

        Command command = this.parser.parse(input);
        IInstruction<T> instruction = this.store.findInstruction(command);

        IArgumentsParser<T> argsParser = instruction.getArgumentsParser();
        T arguments = argsParser.parse(command.getParameter());

        String result = instruction.execute(arguments);
        this.println(result);

    }

}
