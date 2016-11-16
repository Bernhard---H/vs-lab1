package util;

import concurrency.ThreadManager;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class ResourceManager implements CloseMe {

    private ThreadManager threadManager = new ThreadManager();
    private InputStream userRequestStream;
    private PrintStream userResponseStream;
    private Config config;


    public ResourceManager(Config config, InputStream userRequestStream, PrintStream userResponseStream) {
        assert userRequestStream != null;
        assert userResponseStream != null;
        assert config != null;
        this.userRequestStream = userRequestStream;
        this.userResponseStream = userResponseStream;
        this.config = config;
    }

    public ThreadManager getThreadManager() {
        return threadManager;
    }


    public Config getConfig() {
        return config;
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
    public void closeMe() {
        if (this.threadManager != null) {
            CloseMe closeMe = this.threadManager;
            this.threadManager = null;
            closeMe.closeMe();
        }
    }

}
