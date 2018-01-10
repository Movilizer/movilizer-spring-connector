package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerMoveletSet;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class MoveletAttributesCache extends ListJoinCache<MovilizerMoveletAttributeUpdate> {


    MoveletAttributesCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerMoveletAttributeUpdate> getFromRequest(MovilizerRequest request) {
        return request.getMoveletAttributeUpdate();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getMoveletAttributeUpdate().addAll(items);
    }

}
