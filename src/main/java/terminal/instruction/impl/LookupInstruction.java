package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LookupInstruction implements IInstruction<SimpleArgument> {
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
    public String execute(SimpleArgument args, ResourceManager rm) {
        return null;
    }
}
