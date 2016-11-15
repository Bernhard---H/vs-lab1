package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ListInstruction extends AbstractClientInstruction<SimpleArgument> {

    public ListInstruction(ClientResourceManager rm) {
        super(rm);
    }

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
    public String execute(SimpleArgument args) {
        // todo: implement
        return rm.getClient().list();
    }
}
