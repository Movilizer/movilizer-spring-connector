package com.movilizer.connector.v12.persistence.entities.listeners;

import com.movilitas.movilizer.v12.MovilizerMoveletDelete;
import com.movilizer.connector.v12.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.v12.service.CompressorLZ4Service;
import com.movilizer.connector.v12.service.MovilizerXMLParserServiceV12;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.JAXBException;

@Component
public class MoveletToMovilizerQueueCompressorListener {

    private static Log logger = LogFactory.getLog(MoveletToMovilizerQueueCompressorListener.class);

    private MovilizerXMLParserServiceV12 movilizerXMLParserService;

    private CompressorLZ4Service compressorService;

    public MoveletToMovilizerQueueCompressorListener() throws JAXBException {
        movilizerXMLParserService = new MovilizerXMLParserServiceV12();
        movilizerXMLParserService.init();
        compressorService = new CompressorLZ4Service();
    }

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
                final byte[] decompressedXML = movilizerXMLParserService.serializeMovelet(queueRecord.getMovelet());
                if (decompressedXML == null) {
                    logger.error("Cannot compress MaterdataToMovilizerQueue " + queueRecord.toString() +
                            ". No compatible action found.");
                    return;
                }
                queueRecord.setCompressedMovelet(compressorService.compress(decompressedXML));
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
                final byte[] decompressedXML = compressorService.decompress(
                        queueRecord.getCompressedMovelet(), queueRecord.getDecompressedSize());
                queueRecord.setMovelet(movilizerXMLParserService.getMovelet(decompressedXML));
                break;
        }
    }
}
