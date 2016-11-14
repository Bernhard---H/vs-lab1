package network;

import util.BlockingQueueTimeoutException;

import java.util.concurrent.TimeUnit;

/**
 * @author Bernhard Halbartschlager
 */
public interface ConnectionPlus extends Connection {

    String read(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException;

    boolean isClosed();

}
