package com.movilizer.connector.queues;


import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
public class FromMovilizerQueueService  {

    @Resource
    @Qualifier("datacontainerFromMovilizerQueueService")
    private JavaSpringConnectorQueueService<DatacontainerFromMovilizerQueue> datacontainerService;

    //datacontainers
    public boolean offer(DatacontainerFromMovilizerQueue masterdataRecord) {
        return datacontainerService.offer(masterdataRecord);
    }

    public List<DatacontainerFromMovilizerQueue> getAllDatacontainersOrdered() {
        return datacontainerService.getAllOrdered();
    }

    public void removeDatacontainers(Collection<DatacontainerFromMovilizerQueue> records) {
        datacontainerService.remove(records);
    }

    public void removeDatacontainersByKeys(Collection<String> datacontainerKeys) {
        datacontainerService.removeByKeys(datacontainerKeys);
    }

    public DatacontainerFromMovilizerQueue pollDatacontainer() {
        return datacontainerService.poll();
    }

}
