package com.movilizer.connector.v12.service.processors;

public interface Processor<T> {
    public void process(T item);
}
