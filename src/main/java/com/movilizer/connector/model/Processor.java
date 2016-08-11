package com.movilizer.connector.model;

public interface Processor<T> {
    void process(T item);
}
