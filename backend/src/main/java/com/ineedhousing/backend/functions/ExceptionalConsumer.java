package com.ineedhousing.backend.functions;

@FunctionalInterface
public interface ExceptionalConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
