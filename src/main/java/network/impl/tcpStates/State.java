package network.impl.tcpStates;

import network.NetworkException;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public interface State {


    State run(ResourceManager rm) throws NetworkException;

    /**
     * must be callable multiple times and must not throw exceptions
     */
    void close();

}
