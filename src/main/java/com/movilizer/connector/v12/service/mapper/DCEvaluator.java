package com.movilizer.connector.v12.service.mapper;

public interface DCEvaluator<E> {

    public Class<E> getMappingClass();

    public boolean perform(E object);

}
