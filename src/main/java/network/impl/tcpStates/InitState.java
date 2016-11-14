package network.impl.tcpStates;

import network.NetworkException;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class InitState implements State {

    /**
     * do stuff
     * @return next state
     * @throws NetworkException
     */
    @Override
    public State run(ServerResourceManager rm) throws NetworkException {
        return new SetupState();
    }

    /**
     * must be callable multiple times and must not throw exceptions
     */
    @Override
    public void close() {

    }

}
