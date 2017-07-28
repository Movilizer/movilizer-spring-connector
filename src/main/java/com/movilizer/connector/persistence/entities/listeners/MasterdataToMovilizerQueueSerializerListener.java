package com.movilizer.connector.persistence.entities.listeners;

import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class MasterdataToMovilizerQueueSerializerListener extends SerializerListener<MasterdataToMovilizerQueue> {

    private static Log logger = LogFactory.getLog(MasterdataToMovilizerQueueSerializerListener.class);

    public MasterdataToMovilizerQueueSerializerListener() {
    }

    @PreUpdate
    @PrePersist
    @Override
    public void setSerializedFieldsBeforeInsert(MasterdataToMovilizerQueue queueRecord) {
        final byte[] serializedMasterdata;

        switch (queueRecord.getAction()) {
            case DELETE:
                serializedMasterdata = getBytes(queueRecord.getMasterdataDelete());
                break;
            case UPDATE:
                serializedMasterdata = getBytes(queueRecord.getMasterdataUpdate());
                break;
            case REFERENCE:
                serializedMasterdata = getBytes(queueRecord.getMasterdataReference());
                break;
            default:
                serializedMasterdata = null;
                break;
        }
        if (serializedMasterdata == null) {
            logger.error("Cannot serialize MaterdataToMovilizerQueue " + queueRecord.toString() +
                    ". No compatible action found.");
            return;
        }
        queueRecord.setSerializedMasterdata(serializedMasterdata);
    }

    @PostLoad
    @Override
    public void setDeserializedFieldsAfterSelect(MasterdataToMovilizerQueue queueRecord) {
        switch (queueRecord.getAction()) {
            case DELETE:
                queueRecord.setMasterdataDelete(getObject(queueRecord.getSerializedMasterdata()));
                break;
            case UPDATE:
                queueRecord.setMasterdataUpdate(getObject(queueRecord.getSerializedMasterdata()));
                break;
            case REFERENCE:
                queueRecord.setMasterdataReference(getObject(queueRecord.getSerializedMasterdata()));
                break;
        }
    }
}
