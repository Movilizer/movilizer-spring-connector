package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.ArrayList;
import java.util.List;

abstract class ListJoinCache<T> {

    protected final MovilizerMetricService metrics;

    final List<T> items = new ArrayList<>();

    ListJoinCache(MovilizerMetricService metrics) {
        this.metrics = metrics;
    }

    /**
     * Applies the item to the current cache.
     *
     * @param item to be added to the cache
     */
    public void apply(T item) {
        items.add(item);
    }

    /**
     * Add current cache to the outbound request and clears the cache.
     *
     * @param request to add the items to
     * @return if there's a need to send
     */
    public boolean addToRequest(MovilizerRequest request) {
        if (!items.isEmpty()) {
            addListToRequest(request);
            return true;
        }

        items.clear();
        return false;
    }

    protected abstract void addListToRequest(MovilizerRequest request);

    protected abstract Long size();
}
