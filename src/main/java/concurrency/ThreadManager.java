package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bernhard Halbartschlager
 */
public final class ThreadManager {

    private ExecutorService executor = Executors.newCachedThreadPool();


    public void execute(Runnable runnable) {
        this.executor.execute(runnable);
    }

    public void shutdown() {
        this.executor.shutdownNow();
    }



}
