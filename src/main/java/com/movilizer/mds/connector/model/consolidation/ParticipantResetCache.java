package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerParticipantReset;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class ParticipantResetCache extends ListJoinCache<MovilizerParticipantReset> {


    ParticipantResetCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerParticipantReset> getFromRequest(MovilizerRequest request) {
        return request.getParticipantReset();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getParticipantReset().addAll(items);
    }

}
