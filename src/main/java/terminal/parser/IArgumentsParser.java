package terminal.parser;

import terminal.model.IArguments;

/**
 * @author Bernhard Halbartschlager
 */
public interface IArgumentsParser<T extends IArguments> {


    T parse(String parameter);

}
