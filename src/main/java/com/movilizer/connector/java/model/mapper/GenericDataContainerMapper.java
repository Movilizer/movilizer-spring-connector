package com.movilizer.connector.java.model.mapper;

import com.movilitas.movilizer.v12.MovilizerGenericDataContainer;
import com.movilizer.connector.java.exceptions.MovilizerParsingException;

public interface GenericDataContainerMapper {
    <T> T fromDataContainer(MovilizerGenericDataContainer dataContainer, Class<T> objectType) throws MovilizerParsingException;

    <T> boolean isType(MovilizerGenericDataContainer dataContainer, Class<T> objectType) throws MovilizerParsingException;

    <T> Class<T> getType(MovilizerGenericDataContainer dataContainer) throws ClassNotFoundException;

    String getTypeCanonicalName(MovilizerGenericDataContainer dataContainer) throws MovilizerParsingException;

    <T> MovilizerGenericDataContainer toDataContainer(T instanceOfObject);
}
