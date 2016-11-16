package util.impl;

import util.BlockingQueueTimeoutException;
import util.CloseableBlockingQueue;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Bernhard Halbartschlager
 *
 *         methods of this class is not 100% threadsafe to increase performance
 */
public final class MyCloseableBlockingQueue<E> implements CloseableBlockingQueue<E> {

    private BlockingQueue<Container> queue = new LinkedBlockingQueue<>(10);
    private final PoisonContainer poison = new PoisonContainer();
    private boolean closed = false;

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions, returning
     * <tt>true</tt> upon success and throwing an
     * <tt>IllegalStateException</tt> if no space is currently available.
     * When using a capacity-restricted queue, it is generally preferable to
     * use {@link #offer(Object) offer}.
     *
     * @param e the element to add
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     * @throws IllegalStateException if the element cannot be added at this
     * time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     * prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this queue
     */
    @Override
    public boolean add(E e) {
        if (this.closed) {
            throw new IllegalStateException("Queue has been closed");
        }
        return queue.add(new DataContainer(e));
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions, returning
     * <tt>true</tt> upon success and <tt>false</tt> if no space is currently
     * available.  When using a capacity-restricted queue, this method is
     * generally preferable to {@link #add}, which can fail to insert an
     * element only by throwing an exception.
     *
     * @param e the element to add
     * @return <tt>true</tt> if the element was added to this queue, else
     * <tt>false</tt>
     * @throws ClassCastException if the class of the specified element
     * prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this queue
     */
    @Override
    public boolean offer(E e) {
        if (this.closed) {
            throw new IllegalStateException("Queue has been closed");
        }
        return queue.offer(new DataContainer(e));
    }

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     *
     * @param e the element to add
     * @throws InterruptedException if interrupted while waiting
     * @throws ClassCastException if the class of the specified element
     * prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this queue
     */
    @Override
    public void put(E e) throws InterruptedException {
        if (this.closed) {
            throw new IllegalStateException("Queue has been closed");
        }
        queue.put(new DataContainer(e));
    }

    /**
     * Inserts the specified element into this queue, waiting up to the
     * specified wait time if necessary for space to become available.
     *
     * @param e the element to add
     * @param timeout how long to wait before giving up, in units of
     * <tt>unit</tt>
     * @param unit a <tt>TimeUnit</tt> determining how to interpret the
     * <tt>timeout</tt> parameter
     * @return <tt>true</tt> if successful, or <tt>false</tt> if
     * the specified waiting time elapses before space is available
     * @throws InterruptedException if interrupted while waiting
     * @throws ClassCastException if the class of the specified element
     * prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this queue
     */
    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (this.closed) {
            throw new IllegalStateException("Queue has been closed");
        }
        return queue.offer(new DataContainer(e), timeout, unit);
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public E take() throws InterruptedException {
        if (this.closed) {
            this.queue.offer(poison);
        }
        return queue.take().getData();
    }

    /**
     * Retrieves and removes the head of this queue, waiting up to the
     * specified wait time if necessary for an element to become available.
     *
     * @param timeout how long to wait before giving up, in units of
     * <tt>unit</tt>
     * @param unit a <tt>TimeUnit</tt> determining how to interpret the
     * <tt>timeout</tt> parameter
     * @return the head of this queue, or <tt>null</tt> if the
     * specified waiting time elapses before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException {
        if (this.closed) {
            this.queue.offer(poison);
        }
        Container c = this.queue.poll(timeout, unit);
        if (c == null) {
            throw new BlockingQueueTimeoutException("timeout: " + timeout + " " + unit);
        }
        return c.getData();
    }

    /**
     * Returns the number of additional elements that this queue can ideally
     * (in the absence of memory or resource constraints) accept without
     * blocking, or <tt>Integer.MAX_VALUE</tt> if there is no intrinsic
     * limit.
     *
     * <p>Note that you <em>cannot</em> always tell if an attempt to insert
     * an element will succeed by inspecting <tt>remainingCapacity</tt>
     * because it may be the case that another thread is about to
     * insert or remove an element.
     *
     * @return the remaining capacity
     */
    @Override
    public int remainingCapacity() {
        if (this.closed) {
            return 0;
        }
        return queue.remainingCapacity();
    }

    /**
     * Removes a single instance of the specified element from this queue,
     * if it is present.  More formally, removes an element <tt>e</tt> such
     * that <tt>o.equals(e)</tt>, if this queue contains one or more such
     * elements.
     * Returns <tt>true</tt> if this queue contained the specified element
     * (or equivalently, if this queue changed as a result of the call).
     *
     * @param o element to be removed from this queue, if present
     * @return <tt>true</tt> if this queue changed as a result of the call
     * @throws ClassCastException if the class of the specified element
     * is incompatible with this queue
     * (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     * (<a href="../Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean remove(Object o) {
        return queue.remove(new DataContainer((E) o));
    }

    /**
     * Returns <tt>true</tt> if this queue contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this queue contains
     * at least one element <tt>e</tt> such that <tt>o.equals(e)</tt>.
     *
     * @param o object to be checked for containment in this queue
     * @return <tt>true</tt> if this queue contains the specified element
     * @throws ClassCastException if the class of the specified element
     * is incompatible with this queue
     * (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     * (<a href="../Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(Object o) {
        return queue.contains(new DataContainer((E) o));
    }


    /**
     * Retrieves and removes the head of this queue.  This method differs
     * from {@link #poll poll} only in that it throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public E remove() {
        if (this.closed) {
            this.queue.offer(poison);
        }
        return queue.remove().getData();
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    @Override
    public E poll() {
        if (this.closed) {
            this.queue.offer(poison);
        }
        return queue.poll().getData();
    }

    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from {@link #peek peek} only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public E element() {
        return queue.element().getData();
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    @Override
    public E peek() {
        return queue.peek().getData();
    }

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    @Override
    public synchronized int size() {
        if (this.closed) {
            throw new IllegalStateException("BlockingQueue is closed");
        }
        return this.queue.size();
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     * is not supported by this collection
     */
    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public synchronized void closeMe() {
        this.closed = false;
        this.queue.offer(this.poison);
    }

    private abstract class Container {
        public abstract E getData();
    }

    private class DataContainer extends Container {
        private E data;

        public DataContainer(E data) {
            this.data = data;
        }

        public E getData() {
            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            DataContainer that = (DataContainer) o;

            return data != null ? data.equals(that.data) : that.data == null;

        }

        @Override
        public int hashCode() {
            return data != null ? data.hashCode() : 0;
        }
    }

    private class PoisonContainer extends Container {
        @Override
        public E getData() {
            // poison the next thread
            throw new IllegalStateException("BlockingQueue is closed");
        }
    }

}
