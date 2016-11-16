package terminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.exceptions.ParseException;
import terminal.exceptions.impl.CommandNotFoundException;
import terminal.instruction.IInstruction;
import terminal.instruction.IInstructionStore;
import terminal.model.Command;
import terminal.model.IArguments;
import terminal.parser.IArgumentsParser;
import terminal.parser.ICommandParser;
import util.CloseMe;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class Servant<R extends ResourceManager> implements CloseMe, Runnable {

    private static final Log logger = LogFactory.getLog(Servant.class);

    protected IInstructionStore store = null;
    protected ICommandParser parser = null;
    protected R rm;

    public Servant(R rm) {
        assert rm != null;
        this.rm = rm;
    }

    protected <T extends IArguments> String runInput(String input) throws ParseException {
        assert input != null;
        assert this.store != null;
        assert this.parser != null;

        Command command = this.parser.parse(input);
        IInstruction<T> instruction = this.store.findInstruction(command);
        if (instruction == null) {
            throw new CommandNotFoundException("no command '" + command.getName() + "' found");
        }

        IArgumentsParser<T> argsParser = instruction.getArgumentsParser();
        T arguments;
        if (argsParser == null) {
            arguments = null;
        } else {
            arguments = argsParser.parse(command.getParameter());
        }

        return instruction.execute(arguments);
    }

    @Override
    public void closeMe() {

    }

}
