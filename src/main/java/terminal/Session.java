package terminal;

import util.CloseMe;

/**
 * @author Bernhard Halbartschlager
 */
public interface Session extends CloseMe {

    String getName();

    void send(String msg);

    String readln() throws InterruptedException;

}
