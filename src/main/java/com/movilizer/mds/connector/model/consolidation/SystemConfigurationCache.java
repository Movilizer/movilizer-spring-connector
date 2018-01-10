package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerConfigurationContact;
import com.movilitas.movilizer.v15.MovilizerMoveletUpdate;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerSystemConfiguration;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.ArrayList;
import java.util.List;


class SystemConfigurationCache implements ConsolidationCache {

    private final MovilizerMetricService metrics;
    private final List<MovilizerConfigurationContact> items;
    private MovilizerSystemConfiguration configuration;

    SystemConfigurationCache(MovilizerMetricService metrics) {
        this.metrics = metrics;
        items = new ArrayList<>();
        configuration = null;
    }

    @Override
    public void apply(MovilizerRequest request) {
        items.addAll(request.getSystemConfiguration().getConfigurationContact());
        configuration = request.getSystemConfiguration();
    }

    @Override
    public boolean addToRequest(MovilizerRequest request) {
        if (configuration != null) {
            configuration.getConfigurationContact().clear();
            configuration.getConfigurationContact().addAll(items);
            request.setSystemConfiguration(configuration);
            configuration = null;
            items.clear();
            return true;
        }
        return false;
    }

    @Override
    public Long size() {
        return 1L + items.size();
    }
}
