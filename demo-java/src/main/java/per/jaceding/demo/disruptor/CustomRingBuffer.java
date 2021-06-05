package per.jaceding.demo.disruptor;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 基于RingBuffer的并发队列
 *
 * @author jaceding
 * @date 2021/5/18
 */
@SuppressWarnings("all")
public class CustomRingBuffer<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {

    static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE;
    private static final long serialVersionUID = 3904190481728833105L;
    /**
     * 队列大小
     */
    final int size;
    /**
     * 用于存储元素
     */
    final Object[] buffer;


    public CustomRingBuffer(int size) {
        this.size = tableSizeFor(size);
        buffer = new Object[this.size];
    }

    /**
     * Returns a power of two size for the given target capacity.
     */
    static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void put(E e) throws InterruptedException {

    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public E take() throws InterruptedException {
        return null;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }
}
