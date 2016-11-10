package util;

import concurrency.ThreadManager;
import terminal.SessionManager;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class ResourceManager implements CloseMe {

    private ThreadManager threadManager = null;
    private SessionManager sessionManager;
    private InputStream userRequestStream;
    private PrintStream userResponseStream;

    public ResourceManager(SessionManager sessionManager, InputStream userRequestStream, PrintStream userResponseStream) {
        assert sessionManager != null;
        assert userRequestStream != null;
        assert userResponseStream != null;
        this.sessionManager = sessionManager;
        this.userRequestStream = userRequestStream;
        this.userResponseStream = userResponseStream;
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

    public InputStream getUserRequestStream() {
        return userRequestStream;
    }

    public PrintStream getUserResponseStream() {
        return userResponseStream;
    }

    /**
     * Waring: this will try to close everything
     */
    @Override
    public synchronized void closeMe() {
        this.sessionManager.closeMe();
        if (this.threadManager != null) {
            this.threadManager.closeMe();
            this.threadManager = null;
        }
    }

}
