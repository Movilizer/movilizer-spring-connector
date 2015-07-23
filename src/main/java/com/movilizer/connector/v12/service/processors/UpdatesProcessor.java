package com.movilizer.connector.v12.service.processors;


import com.movilitas.movilizer.v14.MovilizerMasterdataPoolUpdate;
import com.movilitas.movilizer.v14.MovilizerRequest;
import com.movilizer.connector.v12.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.v12.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.v12.persistence.entities.ParticipantToMovilizerQueue;
import com.movilizer.connector.v12.service.queues.ToMovilizerQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UpdatesProcessor implements Processor<MovilizerRequest> {

    private static Log logger = LogFactory.getLog(UpdatesProcessor.class);

    @Resource
    private ToMovilizerQueueService toMovilizerQueueService;

    public UpdatesProcessor() {
    }

    @Override
    public void process(MovilizerRequest request) {
        processMasterdata(request);
        processMovelets(request);
        processParticipants(request);
    }

    @Transactional
    private void processMasterdata(MovilizerRequest request) {
        List<MasterdataToMovilizerQueue> masterdataUpdates = toMovilizerQueueService.getAllMasterdataUpdatesOrdered();
        Map<String, MovilizerMasterdataPoolUpdate> poolUpdateMap = new HashMap<>();
        for (MasterdataToMovilizerQueue masterdataUpdate : masterdataUpdates) {
            if (!poolUpdateMap.containsKey(masterdataUpdate.getPool())) {
                MovilizerMasterdataPoolUpdate masterdataPoolUpdate = new MovilizerMasterdataPoolUpdate();
                masterdataPoolUpdate.setPool(masterdataUpdate.getPool());
                poolUpdateMap.put(masterdataUpdate.getPool(), masterdataPoolUpdate);
            }
            masterdataUpdate.addToPoolUpdate(poolUpdateMap.get(masterdataUpdate.getPool()));
        }
        request.getMasterdataPoolUpdate().addAll(poolUpdateMap.values());
        toMovilizerQueueService.removeMasterdata(masterdataUpdates);
    }

    @Transactional
    private void processMovelets(MovilizerRequest request) {
        List<MoveletToMovilizerQueue> moveletUpdates = toMovilizerQueueService.getAllMoveletUpdatesOrdered();
        for (MoveletToMovilizerQueue moveletUpdate : moveletUpdates) {
            moveletUpdate.addToRequest(request);
        }
        toMovilizerQueueService.removeMovelets(moveletUpdates);
    }

    @Transactional
    private void processParticipants(MovilizerRequest request) {
        List<ParticipantToMovilizerQueue> participantUpdates = toMovilizerQueueService.getAllParticipantUpdatesOrdered();
        for (ParticipantToMovilizerQueue participantUpdate : participantUpdates) {
            participantUpdate.addToRequest(request);
        }
        toMovilizerQueueService.removeParticipants(participantUpdates);
    }

}
