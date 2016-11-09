package network;

import network.msg.Message;
import util.CloseMe;

/**
 * @author Bernhard Halbartschlager
 */
public interface Connection extends CloseMe {

    void print(Message msg);

    Message read() throws InterruptedException;

}
