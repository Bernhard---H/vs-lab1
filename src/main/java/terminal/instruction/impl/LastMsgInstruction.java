package terminal.instruction.impl;

import terminal.instruction.IClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LastMsgInstruction implements IClientInstruction<SimpleArgument> {
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
    public String execute(SimpleArgument args, ClientResourceManager rm) {
        return rm.getClient().lastMsg();
    }
}
