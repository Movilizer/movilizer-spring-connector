package com.movilizer.connector.model.mapper;

import com.movilitas.movilizer.v15.MovilizerOnlineDataContainerReply;

public interface OnlineDataContainerReplyMapper <T>{
    MovilizerOnlineDataContainerReply toOnlineDataContainer(T instanceOfObject);
}
