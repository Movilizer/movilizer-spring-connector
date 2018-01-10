package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerMoveletReset;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class MoveletResetCache extends ListJoinCache<MovilizerMoveletReset> {


    MoveletResetCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerMoveletReset> getFromRequest(MovilizerRequest request) {
        return request.getMoveletReset();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getMoveletReset().addAll(items);
    }

}
