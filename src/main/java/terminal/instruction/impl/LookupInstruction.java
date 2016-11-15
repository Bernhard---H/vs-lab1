package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LookupInstruction extends AbstractClientInstruction<SimpleArgument> {

    public LookupInstruction(ClientResourceManager rm) {
        super(rm);
    }

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
    public String execute(SimpleArgument args) {
        return null;
    }
}
