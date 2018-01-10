package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerParticipantConfiguration;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.List;


class ParticipantConfigurationCache extends ListJoinCache<MovilizerParticipantConfiguration> {


    ParticipantConfigurationCache(MovilizerMetricService metrics) {
        super(metrics);
    }

    @Override
    protected List<MovilizerParticipantConfiguration> getFromRequest(MovilizerRequest request) {
        return request.getParticipantConfiguration();
    }

    @Override
    protected void addListToRequest(MovilizerRequest request) {
        request.getParticipantConfiguration().addAll(items);
    }

}
