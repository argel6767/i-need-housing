package com.ineedhousing.utils;

import java.util.ArrayDeque;
import java.util.List;

public class CircularBuffer<T> {
    private ArrayDeque<T> buffer;
    private final int MAX_SIZE = 100;

    public CircularBuffer() {
        buffer = new ArrayDeque<>(MAX_SIZE);
    }

    public void add(T t) {
        if (buffer.size() >= MAX_SIZE) {
            buffer.removeFirst();
        }
        buffer.addLast(t);
    }

    public void clear() {
        buffer.clear();
    }

    public List<T> getMostRecentLogs(int numOfLogs) {
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

    public ArrayDeque<T> getBuffer() {
        return buffer;
    }
}
