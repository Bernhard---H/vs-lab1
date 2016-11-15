package terminal.instruction.impl;

import terminal.instruction.IClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ExitClientInstruction implements IClientInstruction<SimpleArgument> {
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
    public String execute(SimpleArgument args, ClientResourceManager rm) {
        return rm.getClient().exit();
    }
}
