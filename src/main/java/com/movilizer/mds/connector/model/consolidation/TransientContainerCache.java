package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerTransientContainer;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class TransientContainerCache extends ListJoinCache<MovilizerTransientContainer> {


    TransientContainerCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerTransientContainer> getFromRequest(MovilizerRequest request) {
        return request.getTransientContainer();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getTransientContainer().addAll(items);
    }

}
