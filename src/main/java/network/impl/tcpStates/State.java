package network.impl.tcpStates;

import network.NetworkException;

/**
 * @author Bernhard Halbartschlager
 */
public interface State {


    State run() throws NetworkException;

    /**
     * must be callable multiple times and must not throw exceptions
     */
    void close();

}
