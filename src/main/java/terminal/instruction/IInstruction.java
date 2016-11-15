package terminal.instruction;

import terminal.model.IArguments;
import terminal.parser.IArgumentsParser;

/**
 * @author Bernhard Halbartschlager
 */
public interface IInstruction<T extends IArguments> {


    String getName();

    IArgumentsParser<T> getArgumentsParser();

    String execute(T args);

}
