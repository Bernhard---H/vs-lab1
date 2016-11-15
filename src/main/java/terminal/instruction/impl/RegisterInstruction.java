package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.IpAddressArg;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.IpPortParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class RegisterInstruction extends AbstractClientInstruction<IpAddressArg> {

    public RegisterInstruction(ClientResourceManager rm) {
        super(rm);
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
        return rm.getClient().registerAddress(args.getAddress());
    }
}
