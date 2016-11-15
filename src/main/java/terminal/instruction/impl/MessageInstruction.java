package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.PrivateMsgArg;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.PrivateMsgParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class MessageInstruction extends AbstractClientInstruction<PrivateMsgArg> {

    public MessageInstruction(ClientResourceManager rm) {
        super(rm);
    }

    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public IArgumentsParser<PrivateMsgArg> getArgumentsParser() {
        return new PrivateMsgParser();
    }

    @Override
    public String execute(PrivateMsgArg args) {
        return rm.getClient().msg(args.getReceiver(), args.getMessage());
    }

}
