package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.SimpleParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class SendClientInterceptInstruction extends AbstractClientInstruction<SimpleArgument> {

    public SendClientInterceptInstruction(ClientResourceManager rm) {
        super(rm);
    }

    @Override
    public String getName() {
        return "send";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        return new SimpleParser();
    }

    @Override
    public String execute(SimpleArgument args) {
        this.rm.setLastPublicMessage(args.getArgument());
        return args.getArgument();
    }
}
