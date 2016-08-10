package com.movilizer.connector.java.model.mapper;

import com.movilitas.movilizer.v14.MovilizerGenericDataContainer;
import com.movilizer.connector.java.exceptions.MovilizerParsingException;

import java.util.List;

public interface GenericDataContainerMapper {
    <T> T fromDataContainer(MovilizerGenericDataContainer dataContainer, Class<T> objectType) throws MovilizerParsingException;

    <T> boolean isType(MovilizerGenericDataContainer dataContainer, Class<T> objectType) throws MovilizerParsingException;

    <T> Class<T> getType(MovilizerGenericDataContainer dataContainer) throws ClassNotFoundException;

    String getTypeCanonicalName(MovilizerGenericDataContainer dataContainer) throws MovilizerParsingException;

    <T> MovilizerGenericDataContainer toDataContainer(T instanceOfObject);

    <T> MovilizerGenericDataContainer toDataContainer(List<T> instanceOfObject);
}
