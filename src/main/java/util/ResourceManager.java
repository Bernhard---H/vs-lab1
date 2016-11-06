package util;

import concurrency.ThreadManager;
import terminal.SessionManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ResourceManager implements CloseMe {

    private ThreadManager threadManager = null;
    private SessionManager sessionManager;

    public ResourceManager(SessionManager sessionManager) {
        assert sessionManager != null;
        this.sessionManager = sessionManager;
    }

    public ThreadManager getThreadManager() {
        if (this.threadManager == null) {
            this.threadManager = new ThreadManager();
        }
        return threadManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }


    @Override
    public void closeMe() {
        this.sessionManager.closeMe();
        if (this.threadManager != null) {
            this.threadManager.closeMe();
            this.threadManager = null;
        }
    }

}
