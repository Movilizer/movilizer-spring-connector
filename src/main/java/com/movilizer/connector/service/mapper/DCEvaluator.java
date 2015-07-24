package com.movilizer.connector.service.mapper;

public interface DCEvaluator<E> {

    public Class<E> getMappingClass();

    public boolean perform(E object);

}
