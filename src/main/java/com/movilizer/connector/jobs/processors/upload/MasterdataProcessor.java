package com.movilizer.connector.jobs.processors.upload;


import com.movilitas.movilizer.v15.MovilizerMasterdataPoolUpdate;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.connector.model.Processor;
import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.queues.ToMovilizerQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("masterdataProcessor")
public class MasterdataProcessor implements Processor<MovilizerRequest> {
    private static Log logger = LogFactory.getLog(MasterdataProcessor.class);

    @Resource
    private ToMovilizerQueueService toMovilizerQueueService;

    @Transactional
    public void process(MovilizerRequest request) {
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

    public void setToMovilizerQueueService(ToMovilizerQueueService toMovilizerQueueService) {
        this.toMovilizerQueueService = toMovilizerQueueService;
    }
}
