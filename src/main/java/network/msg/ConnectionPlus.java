package network.msg;

import network.Connection;
import util.BlockingQueueTimeoutException;

import java.util.concurrent.TimeUnit;

/**
 * @author Bernhard Halbartschlager
 */
public interface ConnectionPlus extends Connection {

    public String read(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException;

}
