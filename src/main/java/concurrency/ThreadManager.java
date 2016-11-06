package concurrency;

import util.CloseMe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bernhard Halbartschlager
 */
public final class ThreadManager implements CloseMe {

    private ExecutorService executor = Executors.newCachedThreadPool();


    public void execute(Runnable runnable) {
        this.executor.execute(runnable);
    }



    @Override
    public void closeMe() {
        this.executor.shutdownNow();
    }

}
