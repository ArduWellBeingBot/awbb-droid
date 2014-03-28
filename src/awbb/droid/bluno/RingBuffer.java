package awbb.droid.bluno;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class RingBuffer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RingBuffer.class);

    private T[] buffer; // queue elements
    private int count = 0; // number of elements on queue
    private int indexOut = 0; // index of first element of queue
    private int indexIn = 0; // index of next available slot

    // cast needed since no generic array creation in Java
    public RingBuffer(int capacity) {
        buffer = (T[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == buffer.length;
    }

    public int size() {
        return count;
    }

    public void clear() {
        count = 0;
    }

    public void push(T item) {
        if (count == buffer.length) {
            LOGGER.debug("Ring buffer overflow");
            // throw new RuntimeException("Ring buffer overflow");
        }
        buffer[indexIn] = item;
        indexIn = (indexIn + 1) % buffer.length; // wrap-around
        if (count++ == buffer.length) {
            count = buffer.length;
        }
    }

    public T pop() {
        if (isEmpty()) {
            LOGGER.debug("Ring buffer pop underflow");

            // throw new RuntimeException("Ring buffer underflow");
        }
        T item = buffer[indexOut];
        buffer[indexOut] = null; // to help with garbage collection
        if (count-- == 0) {
            count = 0;
        }
        indexOut = (indexOut + 1) % buffer.length; // wrap-around
        return item;
    }

    public T next() {
        if (isEmpty()) {
            LOGGER.debug("Ring buffer next underflow");
            // throw new RuntimeException("Ring buffer underflow");
        }
        return buffer[indexOut];
    }

}