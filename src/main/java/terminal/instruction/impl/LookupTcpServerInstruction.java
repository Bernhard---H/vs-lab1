package terminal.instruction.impl;

import network.model.Address;
import terminal.SessionState;
import terminal.instruction.ASessionServerInstruction;
import terminal.model.Session;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.SimpleParser;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LookupTcpServerInstruction extends ASessionServerInstruction<SimpleArgument> {

    public LookupTcpServerInstruction(ServerResourceManager rm, Session session) {
        super(rm, session);
    }

    @Override
    public String getName() {
        return "lookup";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        return new SimpleParser();
    }

    @Override
    public String execute(SimpleArgument args) {
        if (this.session.getState() != SessionState.AUTHENTICATED) {
            return "ERROR unauthorized operation: please log in first";
        }

        Session session = this.rm.getConnectionManager().getSession(args.getArgument().trim());
        if (session == null){
            return "Wrong username or user not registered.";
        }
        Address address = session.getPrivateAddress();
        if (address == null){
            return "Wrong username or user not registered. (not registered)";
        }
        return address.format();
    }
}
