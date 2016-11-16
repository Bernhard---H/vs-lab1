package terminal.instruction.impl;

import terminal.SessionState;
import terminal.instruction.ASessionServerInstruction;
import terminal.model.Session;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LogoutTcpServerInstruction extends ASessionServerInstruction<SimpleArgument> {


    public LogoutTcpServerInstruction(ServerResourceManager rm, Session session) {
        super(rm, session);
    }

    @Override
    public String getName() {
        return "logout";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        return null;
    }

    @Override
    public String execute(SimpleArgument args) {
        if (this.session.getState() == SessionState.NOBODY) {
            return "ERROR: Not logged in.";
        }

        this.session.setLoggedOut();

        return "Successfully logged out.";
    }
}
