package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ListInstruction implements IInstruction<SimpleArgument> {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        // ignore arguments
        return null;
    }

    @Override
    public String execute(SimpleArgument args, ResourceManager rm) {
        // todo: implement
        return null;
    }
}
