package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ExitClientInterceptInstruction extends AbstractClientInstruction<SimpleArgument> {

    public ExitClientInterceptInstruction(ClientResourceManager rm) {
        super(rm);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        return null;
    }

    @Override
    public String execute(SimpleArgument args) {
        this.rm.closeMe();
        return "remote shutdown by server";
    }
}
