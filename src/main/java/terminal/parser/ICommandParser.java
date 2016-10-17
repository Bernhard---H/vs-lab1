package terminal.parser;

import terminal.model.Command;
import terminal.exceptions.ParseException;

/**
 * @author Bernhard Halbartschlager
 */
public interface ICommandParser {

    /**
     *
     * @param input != null, first character must be an exclamation mark,
     *      followed by a valid command name [a-zA-Z]*
     *      and an optional sequence of parameters
     * @return successfully parsed input string, always != null
     * @throws ParseException
     */
    Command parse(String input) throws ParseException;


}
