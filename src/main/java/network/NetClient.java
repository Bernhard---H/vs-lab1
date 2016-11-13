package network;

import network.msg.ConnectionPlus;

/**
 * @author Bernhard Halbartschlager
 */
public interface NetClient extends ConnectionPlus, Runnable {

    public boolean isClosed();
}
