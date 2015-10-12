package com.movilizer.connector.service.processors;


import com.movilitas.movilizer.v14.MovilizerMetaMoveletReply;
import com.movilitas.movilizer.v14.MovilizerRequest;
import com.movilitas.movilizer.v14.MovilizerResponse;
import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.service.queues.DCFromQueueService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class InboundDataProcessor {

    private static Log logger = LogFactory.getLog(InboundDataProcessor.class);

    @Resource
    private DCFromQueueService fromMovilizerQueueService;

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
/*        for(MovilizerMetaMoveletReply reply : response.getMetaMoveletReply())
        {

        }*/
        request.setRequestAcknowledgeKey(response.getRequestAcknowledgeKey());
    }

    public DCFromQueueService getFromMovilizerQueueService() {
        return fromMovilizerQueueService;
    }

    public void setFromMovilizerQueueService(DCFromQueueService fromMovilizerQueueService) {
        this.fromMovilizerQueueService = fromMovilizerQueueService;
    }
}
