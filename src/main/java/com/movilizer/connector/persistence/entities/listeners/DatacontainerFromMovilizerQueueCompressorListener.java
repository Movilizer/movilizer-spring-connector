package com.movilizer.connector.persistence.entities.listeners;

import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class DatacontainerFromMovilizerQueueCompressorListener {

    @Autowired
    private CompressorListenerService compressor;

    public DatacontainerFromMovilizerQueueCompressorListener() {}

    @PreUpdate
    @PrePersist
    public void setCompressedFieldsBeforeInsert(DatacontainerFromMovilizerQueue queueRecord) {
        final byte[] decompressedXML = compressor.getBytes(queueRecord.getDatacontainer(), MovilizerUploadDataContainer.class);
        queueRecord.setCompressedDatacontainer(compressor.compress(decompressedXML));
        queueRecord.setDecompressedSize(decompressedXML.length);
    }

    @PostLoad
    public void setDecompressedFieldsAfterSelect(DatacontainerFromMovilizerQueue queueRecord) {
        final byte[] decompressedXML = compressor.decompress(queueRecord.getCompressedDatacontainer(), queueRecord.getDecompressedSize());
        queueRecord.setDatacontainer(compressor.getObject(decompressedXML, MovilizerUploadDataContainer.class));
    }
}
