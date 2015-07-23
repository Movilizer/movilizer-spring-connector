package com.movilizer.connector.v12.service.queues;


import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.v12.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.v12.persistence.repositories.DatacontainerFromMovilizerRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FromMovilizerQueueService {

    private static Log logger = LogFactory.getLog(FromMovilizerQueueService.class);

    @Resource
    private DatacontainerFromMovilizerRepository repository;

    @Transactional
    public boolean offer(DatacontainerFromMovilizerQueue datacontainer) {
        if (datacontainer.getKey() == null) {
            logger.error("Tried to save a datacontainer with null key." + datacontainer.toString());
            return false;
        }
        if (!repository.findByKey(datacontainer.getKey()).isEmpty()) {
            logger.info("Duplicate datacontainer from Cloud with key: " + datacontainer.getKey());
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
    public void remove(Collection<MovilizerUploadDataContainer> dataContainers) {
        List<String> keys = new ArrayList<>();
        for (MovilizerUploadDataContainer dataContainer : dataContainers) {
            keys.add(dataContainer.getContainer().getKey());
        }
        repository.deleteByKeyIn(keys);
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
