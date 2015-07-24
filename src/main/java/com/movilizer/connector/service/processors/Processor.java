package com.movilizer.connector.service.processors;

public interface Processor<T> {
    public void process(T item);
}
