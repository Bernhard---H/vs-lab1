package terminal.instruction.impl;

import terminal.instruction.IClientInstruction;
import terminal.model.IpAddressArg;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.IpPortParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class RegisterInstruction implements IClientInstruction<IpAddressArg> {
    @Override
    public String getName() {
        return "register";
    }

    @Override
    public IArgumentsParser<IpAddressArg> getArgumentsParser() {
        return new IpPortParser();
    }

    @Override
    public String execute(IpAddressArg args, ClientResourceManager rm) {
        return rm.getClient().registerAddress(args.getAddress());
    }
}
