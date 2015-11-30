package com.movilizer.connector.java.jobs.processors.upload;


import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilizer.connector.java.model.Processor;
import com.movilizer.connector.java.persistence.entities.ParticipantToMovilizerQueue;
import com.movilizer.connector.java.queues.ToMovilizerQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component("participantProcessor")
public class ParticipantProcessor implements Processor<MovilizerRequest> {
    private static Log logger = LogFactory.getLog(ParticipantProcessor.class);

    @Resource
    private ToMovilizerQueueService toMovilizerQueueService;

    @Transactional
    public void process(MovilizerRequest request) {
        List<ParticipantToMovilizerQueue> participantUpdates = toMovilizerQueueService.getAllParticipantUpdatesOrdered();
        for (ParticipantToMovilizerQueue participantUpdate : participantUpdates) {
            participantUpdate.addToRequest(request);
        }
        toMovilizerQueueService.removeParticipants(participantUpdates);
    }

    public void setToMovilizerQueueService(ToMovilizerQueueService toMovilizerQueueService) {
        this.toMovilizerQueueService = toMovilizerQueueService;
    }
}
