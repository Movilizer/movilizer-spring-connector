package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerMoveletUpdate;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class MoveletUpdateCache extends ListJoinCache<MovilizerMoveletUpdate> {


    MoveletUpdateCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerMoveletUpdate> getFromRequest(MovilizerRequest request) {
        return request.getMoveletUpdate();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getMoveletUpdate().addAll(items);
    }

}
