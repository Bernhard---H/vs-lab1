package terminal.instruction.impl;

import terminal.instruction.IClientInstruction;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.SimpleParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class SendClientInterceptInstruction implements IClientInstruction<SimpleArgument> {
    @Override
    public String getName() {
        return "send";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        return new SimpleParser();
    }

    @Override
    public String execute(SimpleArgument args, ClientResourceManager rm) {
        rm.setLastPublicMessage(args.getArgument());
        return args.getArgument();
    }
}
