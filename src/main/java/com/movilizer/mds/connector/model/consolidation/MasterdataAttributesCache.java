package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMasterdataAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerMoveletAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class MasterdataAttributesCache extends ListJoinCache<MovilizerMasterdataAttributeUpdate> {


    MasterdataAttributesCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerMasterdataAttributeUpdate> getFromRequest(MovilizerRequest request) {
        return request.getMasterdataAttributeUpdate();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getMasterdataAttributeUpdate().addAll(items);
    }

}
