package com.movilizer.connector.v12.service.queues;

import com.movilizer.connector.v12.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.v12.persistence.repositories.MoveletToMovilizerRepository;
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
    public MoveletToMovilizerQueue poll() {
        List<MoveletToMovilizerQueue> all = getAllOrdered();
        if (all.isEmpty())
            return null;
        MoveletToMovilizerQueue oldest = all.get(0);
        repository.delete(oldest);
        return oldest;
    }
}
