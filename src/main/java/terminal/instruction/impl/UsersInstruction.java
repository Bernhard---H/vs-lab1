package terminal.instruction.impl;

import terminal.instruction.AbstractServerInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class UsersInstruction extends AbstractServerInstruction<SimpleArgument> {

    public UsersInstruction(ServerResourceManager rm) {
        super(rm);
    }

    @Override
    public String getName() {
        return "users";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        // ignore args
        return null;
    }

    @Override
    public String execute(SimpleArgument args) {
        return rm.getServer().users();
    }

}
