package com.movilizer.connector.persistence.entities.listeners;

import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.service.CompressorLZ4Service;
import com.movilizer.connector.service.OXMUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.JAXBException;

@Component
public class MaterdataToMovilizerQueueCompressorListener {

    private static Log logger = LogFactory.getLog(MaterdataToMovilizerQueueCompressorListener.class);

    private OXMUtility movilizerXMLParserService;

    private CompressorLZ4Service compressorService;

    public MaterdataToMovilizerQueueCompressorListener() throws JAXBException {
        movilizerXMLParserService = new OXMUtility();
        movilizerXMLParserService.init();
        compressorService = new CompressorLZ4Service();
    }

    @PreUpdate
    @PrePersist
    public void setCompressedFieldsBeforeInsert(MasterdataToMovilizerQueue queueRecord) {
        final byte[] decompressedXML;

        switch (queueRecord.getAction()) {
            case DELETE:
                decompressedXML = movilizerXMLParserService.serializeMasterdataDelete(queueRecord.getMasterdataDelete());
                break;
            case UPDATE:
                decompressedXML = movilizerXMLParserService.serializeMasterdataUpdate(queueRecord.getMasterdataUpdate());
                break;
            case REFERENCE:
                decompressedXML = movilizerXMLParserService.serializeMasterdataReference(queueRecord.getMasterdataReference());
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
        queueRecord.setCompressedMasterdata(compressorService.compress(decompressedXML));
        queueRecord.setDecompressedSize(decompressedXML.length);
    }

    @PostLoad
    public void setDecompressedFieldsAfterSelect(MasterdataToMovilizerQueue queueRecord) {
        final byte[] decompressedXML = compressorService.decompress(
                queueRecord.getCompressedMasterdata(), queueRecord.getDecompressedSize());
        switch (queueRecord.getAction()) {
            case DELETE:
                queueRecord.setMasterdataDelete(movilizerXMLParserService.getMasterdataDelete(decompressedXML));
                break;
            case UPDATE:
                queueRecord.setMasterdataUpdate(movilizerXMLParserService.getMasterdataUpdate(decompressedXML));
                break;
            case REFERENCE:
                queueRecord.setMasterdataReference(movilizerXMLParserService.getMasterdataReference(decompressedXML));
                break;
        }
    }
}
