package terminal.instruction.impl;

import terminal.instruction.IClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LookupInstruction implements IClientInstruction<SimpleArgument> {
    @Override
    public String getName() {
        return "lookup";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        // simple argument is ok
        return null;
    }

    @Override
    public String execute(SimpleArgument args, ClientResourceManager rm) {
        return null;
    }
}
