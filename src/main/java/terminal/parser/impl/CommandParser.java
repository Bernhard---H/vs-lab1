package terminal.parser.impl;

import terminal.exceptions.impl.IllegalFormatException;
import terminal.model.Command;
import terminal.parser.ICommandParser;
import terminal.exceptions.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bernhard Halbartschlager
 */
public final class CommandParser implements ICommandParser {


    /**
     * @param input != null, first character must be an exclamation mark,
     * followed by a valid command name [A-Za-z]*
     * and an optional sequence of parameters
     * @return successfully parsed input string, always != null
     * @throws ParseException
     */
    @Override
    public Command parse(String input) throws ParseException {

        String name, parameter = "";

        Pattern pattern = Pattern.compile("!([A-Za-z]*)");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.find() || matcher.groupCount() < 1 || matcher.group(1).length() < 1) {
            throw new IllegalFormatException("Couldn't find command in input, probably a syntax error");
        }

        name = matcher.group(1);
        if (input.length() > matcher.end(1) + 1) {
            parameter = input.substring(matcher.end(1) + 1);
        }
        // else: no parameters

        return new Command(name, parameter);
    }


    public static void main(String[] args) {
        // regex test

        String testInput = "!login user password";

        Pattern pattern = Pattern.compile("^!([a-z]*)");
        Matcher matcher = pattern.matcher(testInput);

        matcher.find();
        System.out.println("group: " + matcher.group(1));
        System.out.println("start: " + matcher.start(1));
        System.out.println("end: " + matcher.end(1));
        System.out.println(testInput.substring(matcher.end(1) + 1));

    }

}
