package terminal.instruction.impl;

import terminal.instruction.IServerInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ExitServerInstruction implements IServerInstruction<SimpleArgument> {
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
    public String execute(SimpleArgument args, ServerResourceManager rm) {
        return rm.getServer().exit();
    }
}
