package com.movilizer.connector.v12.service.queues;


import com.movilizer.connector.v12.persistence.entities.ParticipantToMovilizerQueue;
import com.movilizer.connector.v12.persistence.repositories.ParticipantToMovilizerRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service("participantToMovilizerQueueService")
public class ParticipantToMovilizerQueueService implements JavaSpringConnectorQueueService<ParticipantToMovilizerQueue> {

    private static Log logger = LogFactory.getLog(ParticipantToMovilizerQueueService.class);

    @Resource
    private ParticipantToMovilizerRepository repository;

    @Transactional
    public boolean offer(ParticipantToMovilizerQueue participantRecord) {
        if (participantRecord.getDeviceAddress() == null) {
            logger.error("Tried to manage a participant with null device address." +
                    participantRecord.toString());
            return false;
        }
        if (participantRecord.getAction() != ParticipantToMovilizerQueue.Action.RESET && participantRecord.getAction() != ParticipantToMovilizerQueue.Action.CONFIGURATION
                && participantRecord.getMoveletKey() == null) {
            logger.error("Tried to manage a participant for null movelet key." +
                    participantRecord.toString());
            return false;
        }
        if (participantRecord.getAction() == ParticipantToMovilizerQueue.Action.CONFIGURATION &&
                ((participantRecord.getParticipantPassword() == null) || (participantRecord.getParticipantPasswordHashType() == null))) {
            logger.error("Tried to manage a participant configuration with null password or password type." +
                    participantRecord.toString());
            return false;
        }
        repository.save(participantRecord);
        return true;
    }

    @Transactional(readOnly = true)
    public List<ParticipantToMovilizerQueue> getAllOrdered() {
        return repository.findAllByOrderBySyncTimestampAsc();
    }

    @Transactional
    public void remove(Collection<ParticipantToMovilizerQueue> records) {
        repository.delete(records);
    }

    @Transactional
    public ParticipantToMovilizerQueue poll() {
        List<ParticipantToMovilizerQueue> all = getAllOrdered();
        if (all.isEmpty())
            return null;
        ParticipantToMovilizerQueue oldest = all.get(0);
        repository.delete(oldest);
        return oldest;
    }
}
