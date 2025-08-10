package com.ineedhousing.backend.model;

import lombok.Getter;

import java.util.ArrayDeque;

@Getter
public class CircularBuffer<T> {
    private final ArrayDeque<T> buffer;
    private final int maxSize; // 100 most recent logs

    public CircularBuffer(int maxSize) {
        this.maxSize = maxSize;
        buffer = new ArrayDeque<>(this.maxSize);
    }

    public void add(T t) {
        if (buffer.size() >= maxSize) {
            buffer.removeFirst();
        }
        buffer.addLast(t);
    }

    public void clear() {
        buffer.clear();
    }
}
