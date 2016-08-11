package com.movilizer.connector.jobs.processors.download;


import com.movilitas.movilizer.v14.MovilizerResponse;
import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.model.Processor;
import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.queues.FromMovilizerQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Component("datacontainerProcessor")
public class DatacontainerProcessor implements Processor<MovilizerResponse> {
    private static Log logger = LogFactory.getLog(DatacontainerProcessor.class);

    @Resource
    private FromMovilizerQueueService fromMovilizerQueueService;

    @Transactional
    public void process(MovilizerResponse response) {
        for (MovilizerUploadDataContainer dataContainer : response.getUploadContainer()) {
            if (dataContainer.getContainer() != null) {
                DatacontainerFromMovilizerQueue dataRecord = new DatacontainerFromMovilizerQueue(dataContainer);
                boolean addedToQueue = fromMovilizerQueueService.offer(dataRecord);
                if (!addedToQueue) {
                    logger.info("Duplicate datacontainer: " + dataRecord.toString());
                }
            }
        }
    }

    public void setFromMovilizerQueueService(FromMovilizerQueueService fromMovilizerQueueService) {
        this.fromMovilizerQueueService = fromMovilizerQueueService;
    }
}
