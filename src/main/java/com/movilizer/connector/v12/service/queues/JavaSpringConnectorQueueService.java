package com.movilizer.connector.v12.service.queues;

import java.util.Collection;
import java.util.List;

public interface JavaSpringConnectorQueueService<T> {
    public boolean offer(T masterdataRecord);

    public List<T> getAllOrdered();

    public void remove(Collection<T> entries);

    public T poll();
}
