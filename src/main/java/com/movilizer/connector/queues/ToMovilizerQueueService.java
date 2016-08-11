package com.movilizer.connector.queues;


import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.persistence.entities.ParticipantToMovilizerQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
public class ToMovilizerQueueService {

    @Resource
    @Qualifier("masterdataToMovilizerQueueService")
    private JavaSpringConnectorQueueService<MasterdataToMovilizerQueue> masterdataService;

    @Resource
    @Qualifier("moveletToMovilizerQueueService")
    private JavaSpringConnectorQueueService<MoveletToMovilizerQueue> moveletService;

    @Resource
    @Qualifier("participantToMovilizerQueueService")
    private JavaSpringConnectorQueueService<ParticipantToMovilizerQueue> participantService;

    //masterdata
    public boolean offer(MasterdataToMovilizerQueue masterdataRecord) {
        return masterdataService.offer(masterdataRecord);
    }

    public List<MasterdataToMovilizerQueue> getAllMasterdataUpdatesOrdered() {
        return masterdataService.getAllOrdered();
    }

    public void removeMasterdata(Collection<MasterdataToMovilizerQueue> records) {
        masterdataService.remove(records);
    }

    public void removeMasterdataByKey(Collection<String> masterdataKeys) {
        masterdataService.removeByKeys(masterdataKeys);
    }

    public MasterdataToMovilizerQueue pollMasterdataUpdate() {
        return masterdataService.poll();
    }

    //movelet
    public boolean offer(MoveletToMovilizerQueue moveletRecord) {
        return moveletService.offer(moveletRecord);
    }

    public List<MoveletToMovilizerQueue> getAllMoveletUpdatesOrdered() {
        return moveletService.getAllOrdered();
    }

    public void removeMovelets(Collection<MoveletToMovilizerQueue> records) {
        moveletService.remove(records);
    }

    public void removeMoveletsByKey(Collection<String> moveletKeys) {
        moveletService.removeByKeys(moveletKeys);
    }

    public MoveletToMovilizerQueue pollMoveletUpdate() {
        return moveletService.poll();
    }

    //participant
    public boolean offer(ParticipantToMovilizerQueue participantRecord) {
        return participantService.offer(participantRecord);
    }

    public List<ParticipantToMovilizerQueue> getAllParticipantUpdatesOrdered() {
        return participantService.getAllOrdered();
    }

    public void removeParticipants(Collection<ParticipantToMovilizerQueue> records) {
        participantService.remove(records);
    }

    public void removeParticipantsByDeviceAddress(Collection<String> deviceAddresses) {
        participantService.removeByKeys(deviceAddresses);
    }

    public ParticipantToMovilizerQueue pollParticipantUpdate() {
        return participantService.poll();
    }
}
