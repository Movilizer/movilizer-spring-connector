package com.movilizer.mds.connector.mapper.datacontainer;

import com.movilitas.movilizer.v15.MovilizerGenericDataContainer;
import com.movilizer.mds.connector.exceptions.MovilizerMappingException;

import java.util.List;

public interface GenericDataContainerMapper {
    <T> T fromDataContainer(MovilizerGenericDataContainer dataContainer, Class<T> objectType) throws MovilizerMappingException;

    <T> boolean isType(MovilizerGenericDataContainer dataContainer, Class<T> objectType) throws MovilizerMappingException;

    <T> Class<T> getType(MovilizerGenericDataContainer dataContainer) throws ClassNotFoundException;

    String getTypeCanonicalName(MovilizerGenericDataContainer dataContainer) throws MovilizerMappingException;

    <T> MovilizerGenericDataContainer toDataContainer(T instanceOfObject);

    <T> MovilizerGenericDataContainer toDataContainer(List<T> instanceOfObject);
}
