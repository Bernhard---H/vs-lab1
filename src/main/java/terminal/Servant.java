package terminal;

import util.CloseMe;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class Servant<R extends ResourceManager> implements CloseMe, Runnable {

    private R rm = null;

    public Servant(R rm) {
        assert rm != null;
        this.rm = rm;
    }


    @Override
    public void closeMe() {
        if (this.rm != null) {
            this.rm.closeMe();
        }
    }

}
