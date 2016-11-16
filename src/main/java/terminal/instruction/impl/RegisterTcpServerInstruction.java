package terminal.instruction.impl;

import terminal.SessionState;
import terminal.instruction.IInstruction;
import terminal.model.IpAddressArg;
import terminal.model.Session;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.IpPortParser;

/**
 * @author Bernhard Halbartschlager
 */
public final class RegisterTcpServerInstruction implements IInstruction<IpAddressArg> {

    private  Session session;

    public RegisterTcpServerInstruction(Session session) {
        assert session != null;
        this.session = session;
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public IArgumentsParser<IpAddressArg> getArgumentsParser() {
        return new IpPortParser();
    }

    @Override
    public String execute(IpAddressArg args) {
        if (this.session.getState() != SessionState.AUTHENTICATED) {
            return "ERROR unauthorized operation: please log in first";
        }
        this.session.registerPrivateAddress(args.getAddress());
        return "Successfully registered address for " + this.session.getName() + ".";
    }
}
