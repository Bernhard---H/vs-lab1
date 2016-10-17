package terminal;

import terminal.instruction.IInstruction;
import terminal.instruction.IInstructionStore;
import terminal.model.Command;
import terminal.model.IArguments;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import terminal.parser.ICommandParser;
import terminal.parser.exceptions.ParseException;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class Servant {


    private IInstructionStore store = null;
    private ICommandParser parser = null;


    public abstract void println(String msg);


    protected <T extends IArguments> void runInput(String input) throws ParseException {
        assert input != null;

        Command<SimpleArgument> command = this.parser.parse(input);
        IInstruction<T> instruction = this.store.findInstruction(command);

        IArgumentsParser<T> argsParser = instruction.getArgumentsParser();
        T arguments = argsParser.parse(command.getParameter().getArgument());

        String result = instruction.execute(arguments);
        this.println(result);
    }

}
