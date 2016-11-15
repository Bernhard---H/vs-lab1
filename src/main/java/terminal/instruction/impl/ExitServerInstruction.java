package terminal.instruction.impl;

import terminal.instruction.AbstractServerInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ExitServerInstruction extends AbstractServerInstruction<SimpleArgument> {

    public ExitServerInstruction(ServerResourceManager rm) {
        super(rm);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        // ignore args
        return null;
    }

    @Override
    public String execute(SimpleArgument args) {
        return rm.getServer().exit();
    }
}
