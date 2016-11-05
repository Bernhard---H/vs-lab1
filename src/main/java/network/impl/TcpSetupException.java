package network.impl;

import network.NetworkException;

/**
 * @author Bernhard Halbartschlager
 */
public final class TcpSetupException extends NetworkException{

    public TcpSetupException(String message, Throwable cause) {
        super(message, cause);
    }

}
