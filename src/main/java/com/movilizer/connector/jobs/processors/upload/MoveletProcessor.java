package com.movilizer.connector.jobs.processors.upload;


import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.connector.model.Processor;
import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.queues.ToMovilizerQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component("moveletProcessor")
public class MoveletProcessor implements Processor<MovilizerRequest> {
    private static Log logger = LogFactory.getLog(MoveletProcessor.class);

    @Resource
    private ToMovilizerQueueService toMovilizerQueueService;

    @Transactional
    public void process(MovilizerRequest request) {
        List<MoveletToMovilizerQueue> moveletUpdates = toMovilizerQueueService.getAllMoveletUpdatesOrdered();
        for (MoveletToMovilizerQueue moveletUpdate : moveletUpdates) {
            moveletUpdate.addToRequest(request);
        }
        toMovilizerQueueService.removeMovelets(moveletUpdates);
    }

    public void setToMovilizerQueueService(ToMovilizerQueueService toMovilizerQueueService) {
        this.toMovilizerQueueService = toMovilizerQueueService;
    }
}
