package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.SimpleParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class SendInstruction extends AbstractClientInstruction<SimpleArgument> {

    public SendInstruction(ClientResourceManager rm) {
        super(rm);
    }

    @Override
    public String getName() {
        return "send";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        // just use simple argument
        return new SimpleParser();
    }

    @Override
    public String execute(SimpleArgument args) {
            return rm.getClient().send(args.getArgument());
    }

}
