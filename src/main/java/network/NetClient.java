package network;

/**
 * @author Bernhard Halbartschlager
 */
public interface NetClient extends ConnectionPlus, Runnable {

    public boolean isClosed();
}
