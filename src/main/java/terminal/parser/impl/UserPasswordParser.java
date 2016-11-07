package terminal.parser.impl;

import terminal.model.UserPasswordArg;
import terminal.exceptions.impl.ArgumentParseException;
import terminal.parser.IArgumentsParser;

/**
 * @author Bernhard Halbartschlager
 */
public final class UserPasswordParser implements IArgumentsParser<UserPasswordArg> {

    @Override
    public UserPasswordArg parse(String parameter) throws ArgumentParseException {
        String[] split = parameter.split(" ", 2);
        if (split.length < 2) {
            throw new ArgumentParseException("username or password is missing");
        }
        return new UserPasswordArg(split[0], split[1]);
    }

}
