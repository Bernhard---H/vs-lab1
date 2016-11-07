package terminal.parser.impl;

import terminal.exceptions.ParseException;
import terminal.exceptions.impl.ArgumentParseException;
import terminal.model.PrivateMsgArg;
import terminal.parser.IArgumentsParser;

/**
 * @author Bernhard Halbartschlager
 */
public final class PrivateMsgParser implements IArgumentsParser<PrivateMsgArg> {

    @Override
    public PrivateMsgArg parse(String parameter) throws ParseException {
        String[] split = parameter.split(" ", 2);
        if (split.length < 2) {
            throw new ArgumentParseException("receiver or message is missing");
        }
        return new PrivateMsgArg(split[0], split[1]);
    }

}
