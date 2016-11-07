package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LastMsgInstruction implements IInstruction<SimpleArgument> {
    @Override
    public String getName() {
        return "lastMsg";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        // ignore arguments
        return null;
    }

    @Override
    public String execute(SimpleArgument args, ResourceManager rm) {
        return rm.getLastPublicMessage();
    }
}
