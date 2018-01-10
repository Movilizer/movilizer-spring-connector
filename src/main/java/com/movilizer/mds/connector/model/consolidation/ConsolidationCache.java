package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerRequest;

interface ConsolidationCache {

    /**
     * Applies the item to the current cache.
     *
     * @param request containing the items to be added to the cache
     */
    void apply(MovilizerRequest request);

    /**
     * Add current cache to the outbound request and clears the cache.
     *
     * @param request to add the items to
     * @return if there's a need to send
     */
    boolean addToRequest(MovilizerRequest request);

    /**
     * Number of items in the current cache (mainly used for metrics).
     *
     * @return number of items in cache
     */
    Long size();
}
