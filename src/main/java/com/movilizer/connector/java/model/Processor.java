package com.movilizer.connector.java.model;

public interface Processor<T> {
    void process(T item);
}
