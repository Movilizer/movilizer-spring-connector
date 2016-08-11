package com.movilizer.connector.queues.upload;

import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.persistence.repositories.MoveletToMovilizerRepository;
import com.movilizer.connector.queues.JavaSpringConnectorQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service("moveletToMovilizerQueueService")
public class MoveletToMovilizerQueueService implements JavaSpringConnectorQueueService<MoveletToMovilizerQueue> {

    private static Log logger = LogFactory.getLog(MoveletToMovilizerQueueService.class);

    @Resource
    private MoveletToMovilizerRepository repository;

    @Transactional
    public boolean offer(MoveletToMovilizerQueue moveletRecord) {
        if (moveletRecord.getMoveletKey() == null) {
            logger.error("Tried to save a movelet with null key." + moveletRecord.toString());
            return false;
        }
        repository.save(moveletRecord);
        return true;
    }

    @Transactional(readOnly = true)
    public List<MoveletToMovilizerQueue> getAllOrdered() {
        return repository.findAllByOrderBySyncTimestampAsc();
    }

    @Transactional
    public void remove(Collection<MoveletToMovilizerQueue> records) {
        repository.delete(records);
    }

    @Transactional
    public void removeByKeys(Collection<String> moveletKeys) {
        repository.deleteByMoveletKeyIn(moveletKeys);
    }

    @Transactional
    public MoveletToMovilizerQueue poll() {
        List<MoveletToMovilizerQueue> all = getAllOrdered();
        if (all.isEmpty())
            return null;
        MoveletToMovilizerQueue oldest = all.get(0);
        repository.delete(oldest);
        return oldest;
    }
}
