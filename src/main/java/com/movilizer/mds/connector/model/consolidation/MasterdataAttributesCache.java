package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMasterdataAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerMoveletAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;


class MasterdataAttributesCache extends ListJoinCache<MovilizerMasterdataAttributeUpdate> {


    MasterdataAttributesCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    protected void addListToRequest(MovilizerRequest request) {
        request.getMasterdataAttributeUpdate().addAll(items);
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
