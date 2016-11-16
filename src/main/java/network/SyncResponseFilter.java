package network;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.BlockingQueueTimeoutException;
import util.ResourceManager;

import java.util.concurrent.TimeUnit;

/**
 * @author Bernhard Halbartschlager
 */
public final class SyncResponseFilter implements ConnectionPlus {

    private static final Log logger = LogFactory.getLog(SyncResponseFilter.class);

    private ConnectionPlus connection;
    private ResourceManager rm;

    public SyncResponseFilter(ConnectionPlus connection, ResourceManager rm) {
        this.connection = connection;
        this.rm = rm;
    }

    @Override
    public void closeMe() {
        connection.closeMe();
    }

    @Override
    public void print(String msg) throws NetworkException {
        connection.print(msg);
    }

    @Override
    public String read() throws InterruptedException {
        String msg;
        do {
            msg = connection.read();
        } while (!this.isResponse(msg));
        return this.stripHeader(msg);
    }

    @Override
    public String read(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException {
        String msg;
        long nanos = unit.toMillis(timeout);
        do {
            // source: http://stackoverflow.com/a/20383130
            long startTime = System.currentTimeMillis();
            msg = connection.read(nanos, TimeUnit.MILLISECONDS);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            if (duration > 0) {
                nanos -= duration;
            }
        } while (!this.isResponse(msg));
        return this.stripHeader(msg);
    }

    @Override
    public boolean isClosed() {
        return connection.isClosed();
    }


    private boolean isResponse(String msg) {
        boolean isResponse = msg.startsWith("!response");
        if (!isResponse) {
            rm.getUserResponseStream().println("ERROR unknown msg: " + msg);
            logger.error("ERROR unknown msg: " + msg);
        }
        return isResponse;
    }

    private String stripHeader(String msg) {
        return msg.substring("!response: ".length());
    }

}
