package com.movilizer.connector.java.jobs.processors.upload;


import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilizer.connector.java.model.Processor;
import com.movilizer.connector.java.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.java.queues.ToMovilizerQueueService;
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
}
