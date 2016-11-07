package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.model.PrivateMsgArg;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.PrivateMsgParser;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class MessageInstruction implements IInstruction<PrivateMsgArg> {

    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public IArgumentsParser<PrivateMsgArg> getArgumentsParser() {
        return new PrivateMsgParser();
    }

    @Override
    public String execute(PrivateMsgArg args, ResourceManager rm) {
        return null;
    }

}
