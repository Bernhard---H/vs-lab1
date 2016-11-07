package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class SendInstruction implements IInstruction<SimpleArgument> {

    @Override
    public String getName() {
        return "send";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        // just use simple argument
        return null;
    }

    @Override
    public String execute(SimpleArgument args, ResourceManager rm) {
        return null;
    }

}
