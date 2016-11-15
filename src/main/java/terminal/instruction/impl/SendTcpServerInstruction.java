package terminal.instruction.impl;

import terminal.SessionState;
import terminal.instruction.IServerInstruction;
import terminal.model.SimpleServerArgument;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.SimpleServerParser;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class SendTcpServerInstruction implements IServerInstruction<SimpleServerArgument> {
    @Override
    public String getName() {
        return "send";
    }

    @Override
    public IArgumentsParser<SimpleServerArgument> getArgumentsParser() {
        return new SimpleServerParser();
    }

    @Override
    public String execute(SimpleServerArgument args, ServerResourceManager rm) {
        if (args.getSession().getState() != SessionState.AUTHENTICATED) {
            return "unauthorized operation: please log in first";
        }
        String msg = "!send "+ args.getSession().getName() + ": "+ args.getArgument();
        rm.getConnectionManager().broadcast(args.getSession(), msg);
        return "server finished broadcasting";
    }

}
