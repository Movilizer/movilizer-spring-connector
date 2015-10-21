package com.movilizer.connector.java.queues.download;

import com.movilizer.connector.java.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.java.persistence.repositories.DatacontainerFromMovilizerRepository;
import com.movilizer.connector.java.queues.JavaSpringConnectorQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service("datacontainerFromMovilizerQueueService")
public class DatacontainerFromMovilizerQueueService implements JavaSpringConnectorQueueService<DatacontainerFromMovilizerQueue> {

    private static Log logger = LogFactory.getLog(DatacontainerFromMovilizerQueueService.class);

    @Resource
    private DatacontainerFromMovilizerRepository repository;

    @Transactional
    public boolean offer(DatacontainerFromMovilizerQueue datacontainer) {
        if (datacontainer.getKey() == null) {
            logger.error("Tried to save a datacontainer with null key." + datacontainer.toString());
            return false;
        }
        if (!repository.findByKey(datacontainer.getKey()).isEmpty()) {
            logger.warn("Duplicate datacontainer from Cloud with key: " + datacontainer.getKey());
            return false;
        }
        repository.save(datacontainer);
        return true;
    }

    @Transactional(readOnly = true)
    public List<DatacontainerFromMovilizerQueue> getAllOrdered() {
        return repository.findAllByOrderByCreationTimestampAsc();
    }

    @Transactional
    public void remove(Collection<DatacontainerFromMovilizerQueue> entries) {
        repository.delete(entries);
    }

    @Transactional
    public void removeByKeys(Collection<String> datacontainerKeys) {
        repository.deleteByKeyIn(datacontainerKeys);
    }

    @Transactional
    public DatacontainerFromMovilizerQueue poll() {
        List<DatacontainerFromMovilizerQueue> all = getAllOrdered();
        if (all.isEmpty())
            return null;
        DatacontainerFromMovilizerQueue oldest = all.get(0);
        repository.delete(oldest);
        return oldest;
    }
}
