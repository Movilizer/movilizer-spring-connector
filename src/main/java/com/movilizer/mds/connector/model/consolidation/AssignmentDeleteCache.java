package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAssignmentDelete;
import com.movilitas.movilizer.v15.MovilizerMoveletSet;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class AssignmentDeleteCache extends ListJoinCache<MovilizerMoveletAssignmentDelete> {


    AssignmentDeleteCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerMoveletAssignmentDelete> getFromRequest(MovilizerRequest request) {
        return request.getMoveletAssignmentDelete();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getMoveletAssignmentDelete().addAll(items);
    }

}
