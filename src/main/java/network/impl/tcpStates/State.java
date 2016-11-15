package network.impl.tcpStates;

import network.NetworkException;
import util.CloseMe;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public interface State extends CloseMe {

    State run(ServerResourceManager rm) throws NetworkException;

}
