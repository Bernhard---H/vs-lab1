package terminal.model;

import java.net.InetAddress;

/**
 * @author Bernhard Halbartschlager
 */
public final class IpAddressArg implements IArguments {

    private InetAddress address;

    public IpAddressArg(InetAddress address) {
        this.address = address;
    }

    public InetAddress getAddress() {
        return address;
    }

}
