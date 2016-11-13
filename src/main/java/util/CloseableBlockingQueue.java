package util;

import java.util.concurrent.TimeUnit;

/**
 * @author Bernhard Halbartschlager
 *
 *         subset interface for java.util.concurrent.BlockingQueue
 */
public interface CloseableBlockingQueue<E> extends CloseMe {
    boolean add(E e);

    boolean offer(E e);

    void put(E e) throws InterruptedException;

    boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException;

    E take() throws InterruptedException;

    E poll();

    E poll(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException;

    E element();

    E peek();

    int remainingCapacity();

    E remove();

    boolean remove(Object o);

    boolean contains(Object o);

    int size();

    void clear();

    boolean isEmpty();

    boolean isClosed();

}
