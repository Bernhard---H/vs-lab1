package terminal.parser.impl;

import terminal.exceptions.ParseException;
import terminal.exceptions.impl.ArgumentParseException;
import terminal.model.IpAddressArg;
import terminal.parser.IArgumentsParser;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Bernhard Halbartschlager
 */
public final class IpPortParser implements IArgumentsParser<IpAddressArg> {

    @Override
    public IpAddressArg parse(String parameter) throws ParseException {
        try {
            return new IpAddressArg(InetAddress.getByName(parameter));
        } catch (UnknownHostException e) {
            throw new ArgumentParseException("unknown host", e);
        }
    }

}
