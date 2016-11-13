package terminal.model;

import network.model.Address;

/**
 * @author Bernhard Halbartschlager
 */
public final class IpAddressArg implements IArguments {

    private Address address;

    public IpAddressArg(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

}
