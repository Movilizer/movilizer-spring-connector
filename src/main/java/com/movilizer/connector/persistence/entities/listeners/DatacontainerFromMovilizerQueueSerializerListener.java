package com.movilizer.connector.persistence.entities.listeners;

import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class DatacontainerFromMovilizerQueueSerializerListener extends SerializerListener<DatacontainerFromMovilizerQueue> {

    public DatacontainerFromMovilizerQueueSerializerListener() {
    }

    @PreUpdate
    @PrePersist
    @Override
    public void setSerializedFieldsBeforeInsert(DatacontainerFromMovilizerQueue queueRecord) {
        final byte[] serializedDatacontainer = getBytes(queueRecord.getDatacontainer());
        queueRecord.setSerializedDatacontainer(serializedDatacontainer);
    }

    @PostLoad
    @Override
    public void setDeserializedFieldsAfterSelect(DatacontainerFromMovilizerQueue queueRecord) {
        queueRecord.setDatacontainer(getObject(queueRecord.getSerializedDatacontainer()));
    }
}
