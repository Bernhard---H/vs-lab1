package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class UsersInstruction implements IInstruction<SimpleArgument> {

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
    public String execute(SimpleArgument args, ResourceManager rm) {
        // todo: implement
        return null;
    }

}
