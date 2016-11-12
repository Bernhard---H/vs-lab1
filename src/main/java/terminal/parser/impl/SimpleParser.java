package terminal.parser.impl;

import terminal.exceptions.ParseException;
import terminal.exceptions.impl.ArgumentParseException;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;

/**
 * @author Bernhard Halbartschlager
 */
public final class SimpleParser implements IArgumentsParser<SimpleArgument> {
    @Override
    public SimpleArgument parse(String parameter) throws ParseException {
        if (parameter.isEmpty()){
            throw new ArgumentParseException("command was expecting a single string argument");
        }
        return new SimpleArgument(parameter);
    }
}
