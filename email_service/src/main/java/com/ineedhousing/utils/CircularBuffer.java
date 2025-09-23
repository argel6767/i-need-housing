package com.ineedhousing.utils;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Circular Buffer that throws out entries after MAX_SIZE is reached
 * @param <T>
 */
public class CircularBuffer<T> {
    private final ConcurrentLinkedDeque<T> buffer;
    private final int MAX_SIZE = 100; // 100 most recent logs

    public CircularBuffer() {
        buffer = new ConcurrentLinkedDeque<>();
    }

    public synchronized void add(T t) {
        if (buffer.size() >= MAX_SIZE) {
            buffer.removeFirst();
        }
        buffer.addLast(t);
    }

    public void clear() {
        buffer.clear();
    }

    public synchronized List<T> getMostRecentLogs(int numOfLogs) {
        if (numOfLogs > MAX_SIZE) {
            throw new IllegalArgumentException("Number of logs requested is too large. The max is " + MAX_SIZE);
        }
        if (numOfLogs < 0) {
            throw new IllegalArgumentException("Number of logs requested is negative");
        }
        if (buffer.size() < numOfLogs) {
            return buffer.stream().toList();
        }

        List<T> bufferList = buffer.stream().toList();
        return bufferList.stream()
                .skip(bufferList.size() - numOfLogs)
                .toList();
    }

    public synchronized ConcurrentLinkedDeque<T> getBuffer() {
        return buffer;
    }
}
