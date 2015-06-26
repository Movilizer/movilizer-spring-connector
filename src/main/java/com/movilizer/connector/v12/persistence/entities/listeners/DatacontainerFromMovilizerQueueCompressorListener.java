package com.movilizer.connector.v12.persistence.entities.listeners;

import com.movilizer.connector.v12.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.v12.service.CompressorLZ4Service;
import com.movilizer.connector.v12.service.MovilizerXMLParserServiceV12;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.JAXBException;

@Component
public class DatacontainerFromMovilizerQueueCompressorListener {

    private MovilizerXMLParserServiceV12 movilizerXMLParserService;

    private CompressorLZ4Service compressorService;

    public DatacontainerFromMovilizerQueueCompressorListener() throws JAXBException {
        movilizerXMLParserService = new MovilizerXMLParserServiceV12();
        movilizerXMLParserService.init();
        compressorService = new CompressorLZ4Service();
    }

    @PreUpdate
    @PrePersist
    public void setCompressedFieldsBeforeInsert(DatacontainerFromMovilizerQueue queueRecord) {
        final byte[] decompressedXML = movilizerXMLParserService.serializeUploadDataContainer(queueRecord.getDatacontainer());
        queueRecord.setCompressedDatacontainer(compressorService.compress(decompressedXML));
        queueRecord.setDecompressedSize(decompressedXML.length);
    }

    @PostLoad
    public void setDecompressedFieldsAfterSelect(DatacontainerFromMovilizerQueue queueRecord) {
        final byte[] decompressedXML = compressorService.decompress(
                queueRecord.getCompressedDatacontainer(), queueRecord.getDecompressedSize());
        queueRecord.setDatacontainer(movilizerXMLParserService.getUploadDataContainer(decompressedXML));
    }
}
