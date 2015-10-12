package com.movilizer.connector.persistence.entities.listeners;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

import com.movilitas.movilizer.v14.MovilizerMetaMoveletReply;
import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.persistence.entities.ReplyFromMovilizerQueue;
import com.movilizer.connector.service.CompressorLZ4Service;
import com.movilizer.connector.service.OXMUtility;

@Component
public class ReplyFromMovilizerQueueCompressorListener {

    private OXMUtility movilizerXMLParserService;

    private CompressorLZ4Service compressorService;

    public ReplyFromMovilizerQueueCompressorListener() throws JAXBException {
        movilizerXMLParserService = new OXMUtility();
        movilizerXMLParserService.init();
        compressorService = new CompressorLZ4Service();
    }

    @PreUpdate
    @PrePersist
    public void setCompressedFieldsBeforeInsert(ReplyFromMovilizerQueue queueRecord) {
        final byte[] decompressedXML = movilizerXMLParserService.serialize(queueRecord.getReply(), MovilizerMetaMoveletReply.class);
        queueRecord.setCompressedDatacontainer(compressorService.compress(decompressedXML));
        queueRecord.setDecompressedSize(decompressedXML.length);
    }

    @PostLoad
    public void setDecompressedFieldsAfterSelect(ReplyFromMovilizerQueue queueRecord) {
        final byte[] decompressedXML = compressorService.decompress(
                queueRecord.getCompressedDatacontainer(), queueRecord.getDecompressedSize());
        queueRecord.setReply(movilizerXMLParserService.deserialize(decompressedXML, MovilizerMetaMoveletReply.class));
    }
}
