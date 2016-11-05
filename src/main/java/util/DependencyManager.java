package util;

import concurrency.ThreadManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class DependencyManager {


    private ThreadManager threadManager = null;


    public ThreadManager getThreadManager() {
        if (this.threadManager == null) {
            this.threadManager = new ThreadManager();
        }
        return threadManager;
    }


}
