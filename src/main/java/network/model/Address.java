package network.model;

/**
 * @author Bernhard Halbartschlager
 */
public final class Address {
    private String host;
    private int port;

    public Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String format() {
        return this.host + ":" + this.port;
    }
}
