package terminal.parser.impl;

import terminal.parser.ICommandParser;
import terminal.parser.exceptions.ParseException;
import terminal.model.Command;
import terminal.model.SimpleArgument;

/**
 * @author Bernhard Halbartschlager
 */
public final class CommandParser implements ICommandParser {


    /**
     *
     * @param input != null, first character must be an exclamation mark,
     *      followed by a valid command name [a-zA-Z]*
     *      and an optional sequence of parameters
     * @return successfully parsed input string, always != null
     * @throws ParseException
     */
    @Override
    public Command<SimpleArgument> parse(String input) throws ParseException {
        // todo
        return null;
    }


}
