package terminal.instruction;

import terminal.model.IArguments;
import terminal.parser.IArgumentsParser;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public interface IInstruction<T extends IArguments, R extends ResourceManager> {


    String getName();

    IArgumentsParser<T> getArgumentsParser();

    String execute(T args, R rm);

}
