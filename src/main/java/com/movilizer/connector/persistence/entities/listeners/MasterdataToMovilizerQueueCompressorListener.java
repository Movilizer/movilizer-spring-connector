package com.movilizer.connector.persistence.entities.listeners;

import com.movilitas.movilizer.v14.MovilizerMasterdataDelete;
import com.movilitas.movilizer.v14.MovilizerMasterdataReference;
import com.movilitas.movilizer.v14.MovilizerMasterdataUpdate;
import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.utils.AutowireHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class MasterdataToMovilizerQueueCompressorListener {

    private static Log logger = LogFactory.getLog(MasterdataToMovilizerQueueCompressorListener.class);

    @Autowired
    private CompressorListenerService compressor;


    public MasterdataToMovilizerQueueCompressorListener() {}

    @PreUpdate
    @PrePersist
    public void setCompressedFieldsBeforeInsert(MasterdataToMovilizerQueue queueRecord) {
    	AutowireHelper.autowire(this, this.compressor);

        final byte[] decompressedXML;

        switch (queueRecord.getAction()) {
            case DELETE:
                decompressedXML = compressor.getBytes(queueRecord.getMasterdataDelete(), MovilizerMasterdataDelete.class);
                break;
            case UPDATE:
                decompressedXML = compressor.getBytes(queueRecord.getMasterdataUpdate(), MovilizerMasterdataUpdate.class);
                break;
            case REFERENCE:
                decompressedXML = compressor.getBytes(queueRecord.getMasterdataReference(), MovilizerMasterdataReference.class);
                break;
            default:
                decompressedXML = null;
                break;
        }
        if (decompressedXML == null) {
            logger.error("Cannot compress MaterdataToMovilizerQueue " + queueRecord.toString() +
                    ". No compatible action found.");
            return;
        }
        queueRecord.setCompressedMasterdata(compressor.compress(decompressedXML));
        queueRecord.setDecompressedSize(decompressedXML.length);
    }

    @PostLoad
    public void setDecompressedFieldsAfterSelect(MasterdataToMovilizerQueue queueRecord) {
    	AutowireHelper.autowire(this, this.compressor);

        final byte[] decompressedXML = compressor.decompress(queueRecord.getCompressedMasterdata(), queueRecord.getDecompressedSize());
        switch (queueRecord.getAction()) {
            case DELETE:
                queueRecord.setMasterdataDelete(compressor.getObject(decompressedXML, MovilizerMasterdataDelete.class));
                break;
            case UPDATE:
                queueRecord.setMasterdataUpdate(compressor.getObject(decompressedXML, MovilizerMasterdataUpdate.class));
                break;
            case REFERENCE:
                queueRecord.setMasterdataReference(compressor.getObject(decompressedXML, MovilizerMasterdataReference.class));
                break;
        }
    }
}
