package com.movilizer.connector.java.persistence.entities.listeners;


import com.movilitas.movilizer.v12.*;
import com.movilizer.connector.java.persistence.entities.ParticipantToMovilizerQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class ParticipantToMovilizerQueueListener {

    private static Log logger = LogFactory.getLog(ParticipantToMovilizerQueueListener.class);

    public ParticipantToMovilizerQueueListener() {
    }

    @PreUpdate
    @PrePersist
    public void setCompressedFieldsBeforeInsert(ParticipantToMovilizerQueue queueRecord) {
        switch (queueRecord.getAction()) {
            case RESET:
                queueRecord.setDeviceAddress(queueRecord.getParticipantReset().getDeviceAddress());
                break;
            case ASSIGN:
                if (!queueRecord.getAssignment().getParticipant().isEmpty()) {
                    MovilizerParticipant participant = queueRecord.getAssignment().getParticipant().get(0);
                    queueRecord.setParticipantKey(participant.getParticipantKey());
                    queueRecord.setName(participant.getName());
                    queueRecord.setDeviceAddress(participant.getDeviceAddress());
                    queueRecord.setResponseQueue(participant.getResponseQueue());
                }
                queueRecord.setMoveletKey(queueRecord.getAssignment().getMoveletKey());
                queueRecord.setMoveletKeyExtension(queueRecord.getAssignment().getMoveletKeyExtension());
                break;
            case ASSIGN_DELETE:
                queueRecord.setDeviceAddress(queueRecord.getAssignmentDelete().getDeviceAddress());
                queueRecord.setMoveletKey(queueRecord.getAssignmentDelete().getMoveletKey());
                queueRecord.setMoveletKeyExtension(queueRecord.getAssignmentDelete().getMoveletKeyExtension());
                queueRecord.setHardDelete(queueRecord.getAssignmentDelete().isHardDelete());
                break;
            case CONFIGURATION:
                queueRecord.setDeviceAddress(queueRecord.getParticipantConfiguration().getDeviceAddress());
                queueRecord.setName(queueRecord.getParticipantConfiguration().getName());
                queueRecord.setParticipantPasswordHashType(queueRecord.getParticipantConfiguration().getPasswordHashType());
                queueRecord.setParticipantPassword(queueRecord.getParticipantConfiguration().getPasswordHashValue());
                break;
        }
    }

    @PostLoad
    public void setDecompressedFieldsAfterSelect(ParticipantToMovilizerQueue queueRecord) {
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
                assignment.setMoveletKeyExtension(queueRecord.getMoveletKeyExtension());
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
                MovilizerParticipantConfiguration movilizerParticipantConf = new MovilizerParticipantConfiguration();
                movilizerParticipantConf.setName(queueRecord.getName());
                movilizerParticipantConf.setDeviceAddress(queueRecord.getDeviceAddress());
                movilizerParticipantConf.setPasswordHashType(queueRecord.getParticipantPasswordHashType()); //Password type (1 - plain text)
                movilizerParticipantConf.setPasswordHashValue(queueRecord.getParticipantPassword());
                queueRecord.setParticipantConfiguration(movilizerParticipantConf);
//                movilizerParticipantConf.setLanguage("en_us");
                break;
        }
    }
}
