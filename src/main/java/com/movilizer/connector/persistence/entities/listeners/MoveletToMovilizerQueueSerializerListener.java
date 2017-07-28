package com.movilizer.connector.persistence.entities.listeners;

import com.movilitas.movilizer.v15.MovilizerMoveletDelete;
import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class MoveletToMovilizerQueueSerializerListener extends SerializerListener<MoveletToMovilizerQueue> {

    private static Log logger = LogFactory.getLog(MoveletToMovilizerQueueSerializerListener.class);

    public MoveletToMovilizerQueueSerializerListener() {
    }


    @PreUpdate
    @PrePersist
    @Override
    public void setSerializedFieldsBeforeInsert(MoveletToMovilizerQueue queueRecord) {
        switch (queueRecord.getAction()) {
            case DELETE:
                queueRecord.setMoveletKey(queueRecord.getMoveletDelete().getMoveletKey());
                queueRecord.setMoveletKeyExtension(queueRecord.getMoveletDelete().getMoveletKeyExtension());
                queueRecord.setIgnoreExtensionKey(queueRecord.getMoveletDelete().isIgnoreExtensionKey());
                break;
            case UPDATE:
                final byte[] serializedMovelet = getBytes(queueRecord.getMovelet());
                if (serializedMovelet == null) {
                    logger.error("Cannot serialize MoveletToMovilizerQueue " + queueRecord.toString()
                            + ". No compatible action found.");
                    return;
                }
                queueRecord.setSerializedMovelet(serializedMovelet);
                break;
        }
    }

    @PostLoad
    @Override
    public void setDeserializedFieldsAfterSelect(MoveletToMovilizerQueue queueRecord) {
        switch (queueRecord.getAction()) {
            case DELETE:
                MovilizerMoveletDelete moveletDelete = new MovilizerMoveletDelete();
                moveletDelete.setMoveletKey(queueRecord.getMoveletKey());
                moveletDelete.setMoveletKeyExtension(queueRecord.getMoveletKeyExtension());
                moveletDelete.setIgnoreExtensionKey(queueRecord.getIgnoreExtensionKey());
                queueRecord.setMoveletDelete(moveletDelete);
                break;
            case UPDATE:
                queueRecord.setMovelet(getObject(queueRecord.getSerializedMovelet()));
                break;
        }
    }
}
