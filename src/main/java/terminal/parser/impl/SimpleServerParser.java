package terminal.parser.impl;

import terminal.exceptions.ParseException;
import terminal.exceptions.impl.ArgumentParseException;
import terminal.model.SimpleServerArgument;
import terminal.parser.IArgumentsParser;

/**
 * @author Bernhard Halbartschlager
 */
public final class SimpleServerParser implements IArgumentsParser<SimpleServerArgument> {
    @Override
    public SimpleServerArgument parse(String parameter) throws ParseException {
        if (parameter.isEmpty()){
            throw new ArgumentParseException("command was expecting a single string argument");
        }
        return new SimpleServerArgument(parameter);
    }
}
