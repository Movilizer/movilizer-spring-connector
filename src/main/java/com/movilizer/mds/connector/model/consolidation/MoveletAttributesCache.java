package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerMoveletSet;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;


class MoveletAttributesCache extends ListJoinCache<MovilizerMoveletAttributeUpdate> {


    MoveletAttributesCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    protected void addListToRequest(MovilizerRequest request) {
        request.getMoveletAttributeUpdate().addAll(items);
    }

    /**
     * Number of movelet attribute updates in the current cache (mainly used for metrics).
     *
     * @return number of movelet attribute update in cache
     */
    protected Long size() {
        return (long) items.size();
    }
}
