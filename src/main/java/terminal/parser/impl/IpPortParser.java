package terminal.parser.impl;

import network.model.Address;
import terminal.exceptions.ParseException;
import terminal.exceptions.impl.ArgumentParseException;
import terminal.model.IpAddressArg;
import terminal.parser.IArgumentsParser;

/**
 * @author Bernhard Halbartschlager
 */
public final class IpPortParser implements IArgumentsParser<IpAddressArg> {

    @Override
    public IpAddressArg parse(String parameter) throws ParseException {
        String[] split = parameter.split(":", 2);
        if(split.length < 2){
            throw new ArgumentParseException("input must have the format:  address:port ");
        }
        try {
            return new IpAddressArg(new Address(split[0], Integer.parseInt(split[1])));
        } catch (NumberFormatException e){
            throw new ArgumentParseException("port must be an integer", e);
        }
    }

}
