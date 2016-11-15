package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ExitClientInstruction extends AbstractClientInstruction<SimpleArgument> {

    public ExitClientInstruction(ClientResourceManager rm) {
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
        return rm.getClient().exit();
    }
}
