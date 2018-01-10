package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerMoveletClone;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class MoveletCloneCache extends ListJoinCache<MovilizerMoveletClone> {


    MoveletCloneCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerMoveletClone> getFromRequest(MovilizerRequest request) {
        return request.getMoveletClone();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getMoveletClone().addAll(items);
    }

}
