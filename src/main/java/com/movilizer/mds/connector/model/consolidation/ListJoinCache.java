package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.ArrayList;
import java.util.List;

abstract class ListJoinCache<T> implements ConsolidationCache {

    protected final MovilizerMetricService metrics;

    final List<T> items = new ArrayList<>();

    ListJoinCache(MovilizerMetricService metrics) {
        this.metrics = metrics;
    }

    /**
     * Applies the item to the current cache.
     *
     * @param request containing the items to be added to the cache
     */
    public void apply(MovilizerRequest request) {
        items.addAll(getFromRequest(request));
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
            items.clear();
            return true;
        }
        return false;
    }

    protected abstract List<T> getFromRequest(MovilizerRequest request);
    protected abstract void addListToRequest(MovilizerRequest request);

    /**
     * Number of items in the current cache (mainly used for metrics).
     *
     * @return number of items in cache
     */
    public Long size() {
        return (long) items.size();
    }
}
