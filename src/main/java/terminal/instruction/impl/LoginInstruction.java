package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.model.UserPasswordArg;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.UserPasswordParser;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LoginInstruction implements IInstruction<UserPasswordArg, ClientResourceManager> {
    @Override
    public String getName() {
        return "login";
    }

    @Override
    public IArgumentsParser<UserPasswordArg> getArgumentsParser() {
        return new UserPasswordParser();
    }

    @Override
    public String execute(UserPasswordArg args, ClientResourceManager rm) {
        return rm.getClient().login(args.getUsername(), args.getPassword());
    }
}
