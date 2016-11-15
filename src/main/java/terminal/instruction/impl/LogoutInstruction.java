package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LogoutInstruction extends AbstractClientInstruction<SimpleArgument> {

    public LogoutInstruction(ClientResourceManager rm) {
        super(rm);
    }

    @Override
    public String getName() {
        return "logout";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        // ignore arguments
        return null;
    }

    @Override
    public String execute(SimpleArgument args) {
            return rm.getClient().logout();
    }
}
