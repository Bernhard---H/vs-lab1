package network.impl.tcpStates;

import network.NetworkException;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public interface State {


    State run(ServerResourceManager rm) throws NetworkException;

    /**
     * must be callable multiple times and must not throw exceptions
     */
    void close();

}
