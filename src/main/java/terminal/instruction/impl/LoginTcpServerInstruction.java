package terminal.instruction.impl;

import terminal.SessionState;
import terminal.instruction.ASessionServerInstruction;
import terminal.model.Session;
import terminal.model.UserPasswordArg;
import terminal.parser.IArgumentsParser;
import terminal.parser.impl.UserPasswordParser;
import util.Config;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class LoginTcpServerInstruction extends ASessionServerInstruction<UserPasswordArg> {


    public LoginTcpServerInstruction(ServerResourceManager rm, Session session) {
        super(rm, session);
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
        if (this.session.getState() == SessionState.AUTHENTICATED) {
            return "ERROR: Already logged in.";
        }

        Config users = this.rm.getUsers();
        if (!users.listKeys().contains(args.getUsername()+".password")) {
            return "ERROR: Wrong username or password.  User not found";
        }

        if (!users.getString(args.getUsername()+".password").equals(args.getPassword())) {
            return "ERROR: Wrong username or password.  wrong password";
        }
        this.session.setAuthenticated(args.getUsername());

        return "Successfully logged in.";
    }
}
