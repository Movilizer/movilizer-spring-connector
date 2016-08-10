package com.movilizer.connector.java.model.mapper;

import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;

public interface DataContainerMapper <T>{
    T fromDataContainer(MovilizerUploadDataContainer dataContainer, Class<T> objectType);

    MovilizerUploadDataContainer toDataContainer(T instanceOfObject);
}
