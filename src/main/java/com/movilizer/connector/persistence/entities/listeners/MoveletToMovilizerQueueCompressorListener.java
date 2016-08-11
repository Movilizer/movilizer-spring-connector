package com.movilizer.connector.persistence.entities.listeners;

import com.movilitas.movilizer.v14.MovilizerMovelet;
import com.movilitas.movilizer.v14.MovilizerMoveletDelete;
import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class MoveletToMovilizerQueueCompressorListener {

    private static Log logger = LogFactory.getLog(MoveletToMovilizerQueueCompressorListener.class);

    @Autowired
    private CompressorListenerService compressor;

    public MoveletToMovilizerQueueCompressorListener() {}

    @PreUpdate
    @PrePersist
    public void setCompressedFieldsBeforeInsert(MoveletToMovilizerQueue queueRecord) {
        switch (queueRecord.getAction()) {
            case DELETE:
                queueRecord.setMoveletKey(queueRecord.getMoveletDelete().getMoveletKey());
                queueRecord.setMoveletKeyExtension(queueRecord.getMoveletDelete().getMoveletKeyExtension());
                queueRecord.setIgnoreExtensionKey(queueRecord.getMoveletDelete().isIgnoreExtensionKey());
                break;
            case UPDATE:
                final byte[] decompressedXML = compressor.getBytes(queueRecord.getMovelet(), MovilizerMovelet.class);
                if (decompressedXML == null) {
                    logger.error("Cannot compress MaterdataToMovilizerQueue " + queueRecord.toString() +
                            ". No compatible action found.");
                    return;
                }
                queueRecord.setCompressedMovelet(compressor.compress(decompressedXML));
                queueRecord.setDecompressedSize(decompressedXML.length);
                break;
        }
    }

    @PostLoad
    public void setDecompressedFieldsAfterSelect(MoveletToMovilizerQueue queueRecord) {
        switch (queueRecord.getAction()) {
            case DELETE:
                MovilizerMoveletDelete moveletDelete = new MovilizerMoveletDelete();
                moveletDelete.setMoveletKey(queueRecord.getMoveletKey());
                moveletDelete.setMoveletKeyExtension(queueRecord.getMoveletKeyExtension());
                moveletDelete.setIgnoreExtensionKey(queueRecord.getIgnoreExtensionKey());
                queueRecord.setMoveletDelete(moveletDelete);
                break;
            case UPDATE:
                final byte[] decompressedXML = compressor.decompress(
                        queueRecord.getCompressedMovelet(), queueRecord.getDecompressedSize());
                queueRecord.setMovelet(compressor.getObject(decompressedXML, MovilizerMovelet.class));
                break;
        }
    }
}
