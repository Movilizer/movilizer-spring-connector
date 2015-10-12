package com.movilizer.connector.persistence.entities.listeners;

import java.io.ByteArrayInputStream;

import com.movilitas.movilizer.v14.*;
import com.movilizer.connector.persistence.entities.ParticipantToMovilizerQueue;
import com.movilizer.connector.service.CompressorLZ4Service;
import com.movilizer.connector.service.OXMUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class ParticipantToMovilizerQueueListener {

    private static Log logger = LogFactory
            .getLog(ParticipantToMovilizerQueueListener.class);

    @Autowired
    private OXMUtility utilityOXM;

    @Autowired
    private CompressorLZ4Service compressorService;

    public ParticipantToMovilizerQueueListener() {
        utilityOXM = new OXMUtility();
        compressorService = new CompressorLZ4Service();
    }

    @PreUpdate
    @PrePersist
    public void setCompressedFieldsBeforeInsert(
            ParticipantToMovilizerQueue queueRecord) {
        switch (queueRecord.getAction()) {
        case RESET:
            queueRecord.setDeviceAddress(queueRecord.getParticipantReset()
                    .getDeviceAddress());
            break;
        case ASSIGN:
            if (!queueRecord.getAssignment().getParticipant().isEmpty()) {
                MovilizerParticipant participant = queueRecord.getAssignment()
                        .getParticipant().get(0);
                queueRecord.setParticipantKey(participant.getParticipantKey());
                queueRecord.setName(participant.getName());
                queueRecord.setDeviceAddress(participant.getDeviceAddress());
                queueRecord.setResponseQueue(participant.getResponseQueue());
            }
            queueRecord.setMoveletKey(queueRecord.getAssignment()
                    .getMoveletKey());
            queueRecord.setMoveletKeyExtension(queueRecord.getAssignment()
                    .getMoveletKeyExtension());
            break;
        case ASSIGN_DELETE:
            queueRecord.setDeviceAddress(queueRecord.getAssignmentDelete()
                    .getDeviceAddress());
            queueRecord.setMoveletKey(queueRecord.getAssignmentDelete()
                    .getMoveletKey());
            queueRecord.setMoveletKeyExtension(queueRecord
                    .getAssignmentDelete().getMoveletKeyExtension());
            queueRecord.setHardDelete(queueRecord.getAssignmentDelete()
                    .isHardDelete());
            break;
        case CONFIGURATION:
            queueRecord.setDeviceAddress(queueRecord
                    .getParticipantConfiguration().getDeviceAddress());

            final byte[] decompressedXML = utilityOXM
                    .toByteOutputStream(
                            new ObjectFactory()
                                    .createMovilizerParticipantConfiguration(queueRecord
                                            .getParticipantConfiguration()), MovilizerParticipantConfiguration.class)
                    .toByteArray();

            queueRecord.setCompressedConfiguration(compressorService
                    .compress(decompressedXML));
            queueRecord.setDecompressedSize(decompressedXML.length);

            break;
        }
    }

    @PostLoad
    public void setDecompressedFieldsAfterSelect(
            ParticipantToMovilizerQueue queueRecord) {
        switch (queueRecord.getAction()) {
        case RESET:
            MovilizerParticipantReset reset = new MovilizerParticipantReset();
            reset.setDeviceAddress(queueRecord.getDeviceAddress());
            queueRecord.setParticipantReset(reset);
            break;
        case ASSIGN:
            MovilizerMoveletAssignment assignment = new MovilizerMoveletAssignment();
            if (queueRecord.getDeviceAddress() != null) {
                MovilizerParticipant participant = new MovilizerParticipant();
                participant.setParticipantKey(queueRecord.getParticipantKey());
                participant.setName(queueRecord.getName());
                participant.setDeviceAddress(queueRecord.getDeviceAddress());
                participant.setResponseQueue(queueRecord.getResponseQueue());
                assignment.getParticipant().add(participant);
            }
            assignment.setMoveletKey(queueRecord.getMoveletKey());
            assignment.setMoveletKeyExtension(queueRecord
                    .getMoveletKeyExtension());
            queueRecord.setAssignment(assignment);
            break;
        case ASSIGN_DELETE:
            MovilizerMoveletAssignmentDelete delete = new MovilizerMoveletAssignmentDelete();
            delete.setDeviceAddress(queueRecord.getDeviceAddress());
            delete.setMoveletKey(queueRecord.getMoveletKey());
            delete.setMoveletKeyExtension(queueRecord.getMoveletKeyExtension());
            delete.setHardDelete(queueRecord.getHardDelete());
            queueRecord.setAssignmentDelete(delete);
            break;
        case CONFIGURATION:
            final byte[] decompressedXML = compressorService.decompress(
                    queueRecord.getCompressedConfiguration(), queueRecord.getDecompressedSize());
            MovilizerParticipantConfiguration movilizerParticipantConf = utilityOXM
                    .inputStreamToObject(
                            new ByteArrayInputStream(decompressedXML),
                            MovilizerParticipantConfiguration.class);
            queueRecord.setParticipantConfiguration(movilizerParticipantConf);
            break;
        }
    }
}
