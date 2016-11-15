package terminal.instruction.impl;

import terminal.SessionState;
import terminal.instruction.AbstractServerInstruction;
import terminal.model.Session;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.SimpleParser;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class SendTcpServerInstruction extends AbstractServerInstruction<SimpleArgument> {

    private Session session;

    public SendTcpServerInstruction(ServerResourceManager rm, Session session) {
        super(rm);
        assert session != null;
        this.session = session;
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
        if (this.session.getState() != SessionState.AUTHENTICATED) {
            return "unauthorized operation: please log in first";
        }
        String msg = "!send "+ this.session.getName() + ": "+ args.getArgument();
        rm.getConnectionManager().broadcast(this.session, msg);
        return "server finished broadcasting";
    }

}
