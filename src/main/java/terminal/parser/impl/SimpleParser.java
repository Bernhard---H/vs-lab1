package terminal.parser.impl;

import terminal.exceptions.ParseException;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;

/**
 * @author Bernhard Halbartschlager
 */
public final class SimpleParser implements IArgumentsParser<SimpleArgument> {
    @Override
    public SimpleArgument parse(String parameter) throws ParseException {
        return new SimpleArgument(parameter);
    }
}
