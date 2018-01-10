package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAssignment;
import com.movilitas.movilizer.v15.MovilizerMoveletAssignmentDelete;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.ArrayList;
import java.util.List;


class AssignmentCache extends ListJoinCache<MovilizerMoveletAssignment> {


    AssignmentCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerMoveletAssignment> getFromRequest(MovilizerRequest request) {
        return request.getMoveletAssignment();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getMoveletAssignment().addAll(items);
    }

}
