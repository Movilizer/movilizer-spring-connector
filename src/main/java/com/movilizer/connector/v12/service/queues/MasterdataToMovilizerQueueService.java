package com.movilizer.connector.v12.service.queues;

import com.movilizer.connector.v12.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.v12.persistence.repositories.MasterdataToMovilizerRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service("masterdataToMovilizerQueueService")
public class MasterdataToMovilizerQueueService implements JavaSpringConnectorQueueService<MasterdataToMovilizerQueue> {

    private static Log logger = LogFactory.getLog(MasterdataToMovilizerQueueService.class);

    @Resource
    private MasterdataToMovilizerRepository repository;

    @Transactional
    public boolean offer(MasterdataToMovilizerQueue masterdataRecord) {
        if (masterdataRecord.getPool() == null) {
            logger.error("Tried to save a masterdata with null pool." + masterdataRecord.toString());
            return false;
        }
        if (masterdataRecord.getKey() == null &&
                masterdataRecord.getAction() == MasterdataToMovilizerQueue.Action.UPDATE) {
            logger.error("Tried to save a masterdata with null key." + masterdataRecord.toString());
            return false;
        }
        if (masterdataRecord.getGroup() == null &&
                masterdataRecord.getAction() == MasterdataToMovilizerQueue.Action.UPDATE) {
            logger.error("Tried to save a masterdata with null group." + masterdataRecord.toString());
            return false;
        }
        repository.save(masterdataRecord);
        return true;
    }

    @Transactional(readOnly = true)
    public List<MasterdataToMovilizerQueue> getAllOrdered() {
        return repository.findAllByOrderBySyncTimestampAsc();
    }

    @Transactional
    public void remove(Collection<MasterdataToMovilizerQueue> records) {
        repository.delete(records);
    }

    @Transactional
    public MasterdataToMovilizerQueue poll() {
        List<MasterdataToMovilizerQueue> all = getAllOrdered();
        if (all.isEmpty())
            return null;
        MasterdataToMovilizerQueue oldest = all.get(0);
        repository.delete(oldest);
        return oldest;
    }
}
