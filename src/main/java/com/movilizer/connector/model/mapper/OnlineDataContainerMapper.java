package com.movilizer.connector.model.mapper;

import com.movilitas.movilizer.v15.MovilizerOnlineDataContainer;

public interface OnlineDataContainerMapper <T>{
    T fromOnlineDataContainer(MovilizerOnlineDataContainer dataContainer, Class<T> objectType);
}
