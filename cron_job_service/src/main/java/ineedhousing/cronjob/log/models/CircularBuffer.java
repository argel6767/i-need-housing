package ineedhousing.cronjob.log.models;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Circular Buffer to keep track of the MAX_SIZE most recent logs
 * @param <T>
 */
public class CircularBuffer<T> {
    private final ConcurrentLinkedDeque<T> buffer;
    private int maxSize = 100; // 100 most recent logs

    public CircularBuffer() {
        buffer = new ConcurrentLinkedDeque<>();
    }

    public CircularBuffer(int maxSize) {
        this.maxSize = maxSize;
        buffer = new ConcurrentLinkedDeque<>();
    }

    public void add(T t) {
        if (buffer.size() >= maxSize) {
            buffer.removeFirst();
        }
        buffer.addLast(t);
    }

    public synchronized void clear() {
        buffer.clear();
    }

    public synchronized List<T> getMostRecentEntries(int numOfEntries) {
        if (numOfEntries > maxSize) {
            throw new IllegalArgumentException("Number of entries requested is too large. The max is " + maxSize);
        }
        if (numOfEntries < 0) {
            throw new IllegalArgumentException("Number of entries requested is negative. What is a negative amount of logs?");
        }
        if (buffer.size() < numOfEntries) {
            return buffer.stream().toList();
        }

        List<T> bufferList = buffer.stream().toList();
        return bufferList.stream()
                .skip(bufferList.size() - numOfEntries)
                .toList();
    }

    public synchronized ConcurrentLinkedDeque<T> getBuffer() {
        return buffer;
    }
}
