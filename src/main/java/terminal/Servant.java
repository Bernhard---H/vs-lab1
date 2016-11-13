package terminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.CloseMe;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class Servant<R extends ResourceManager> implements CloseMe, Runnable {

    private static final Log logger = LogFactory.getLog(Servant.class);

    protected R rm;

    public Servant(R rm) {
        assert rm != null;
        this.rm = rm;
    }

    @Override
    public synchronized void closeMe() {
        if (this.rm != null) {
            // just in case I ever create a circle
            CloseMe closeable = this.rm;
            this.rm = null;
            closeable.closeMe();
        }
    }

}
