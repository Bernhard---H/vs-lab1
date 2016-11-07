package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.model.IpAddressArg;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.IpPortParser;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class RegisterInstruction implements IInstruction<IpAddressArg> {
    @Override
    public String getName() {
        return "register";
    }

    @Override
    public IArgumentsParser<IpAddressArg> getArgumentsParser() {
        return new IpPortParser();
    }

    @Override
    public String execute(IpAddressArg args, ResourceManager rm) {
        // todo: implement
        return null;
    }
}
