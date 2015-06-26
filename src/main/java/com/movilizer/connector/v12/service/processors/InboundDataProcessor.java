package com.movilizer.connector.v12.service.processors;


import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
import com.movilizer.connector.v12.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.v12.service.queues.FromMovilizerQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class InboundDataProcessor {

    private static Log logger = LogFactory.getLog(InboundDataProcessor.class);

    @Resource
    private FromMovilizerQueueService fromMovilizerQueueService;

    public InboundDataProcessor() {
    }

    @Transactional
    public void process(MovilizerResponse response, MovilizerRequest request) {
        for (MovilizerUploadDataContainer dataContainer : response.getUploadContainer()) {
            DatacontainerFromMovilizerQueue dataRecord = new DatacontainerFromMovilizerQueue(dataContainer);
            boolean addedToQueue = fromMovilizerQueueService.offer(dataRecord);
            if (!addedToQueue) {
                logger.info("Duplicate datacontainer: " + dataRecord.toString());
            }
        }
        request.setRequestAcknowledgeKey(response.getRequestAcknowledgeKey());
    }

    public FromMovilizerQueueService getFromMovilizerQueueService() {
        return fromMovilizerQueueService;
    }

    public void setFromMovilizerQueueService(FromMovilizerQueueService fromMovilizerQueueService) {
        this.fromMovilizerQueueService = fromMovilizerQueueService;
    }
}
