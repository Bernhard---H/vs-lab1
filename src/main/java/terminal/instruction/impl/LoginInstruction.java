package terminal.instruction.impl;

import terminal.instruction.AbstractClientInstruction;
import terminal.model.UserPasswordArg;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.UserPasswordParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LoginInstruction extends AbstractClientInstruction<UserPasswordArg> {

    public LoginInstruction(ClientResourceManager rm) {
        super(rm);
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public IArgumentsParser<UserPasswordArg> getArgumentsParser() {
        return new UserPasswordParser();
    }

    @Override
    public String execute(UserPasswordArg args) {
        return rm.getClient().login(args.getUsername(), args.getPassword());
    }
}
