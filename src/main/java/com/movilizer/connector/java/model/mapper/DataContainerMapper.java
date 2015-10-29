package com.movilizer.connector.java.model.mapper;

import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;

public interface DataContainerMapper <T>{
    T fromDataContainer(MovilizerUploadDataContainer dataContainer, Class<T> objectType);

    MovilizerUploadDataContainer toDataContainer(T instanceOfObject);
}
