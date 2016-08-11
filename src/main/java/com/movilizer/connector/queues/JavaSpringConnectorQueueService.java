package com.movilizer.connector.queues;

import java.util.Collection;
import java.util.List;

public interface JavaSpringConnectorQueueService<T> {
    boolean offer(T masterdataRecord);

    List<T> getAllOrdered();

    void remove(Collection<T> entries);

    void removeByKeys(Collection<String> entries);

    T poll();
}
