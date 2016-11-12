package network;

import util.CloseMe;

/**
 * @author Bernhard Halbartschlager
 */
public interface Connection extends CloseMe {

    void print(String msg);

    String read() throws InterruptedException;

}
