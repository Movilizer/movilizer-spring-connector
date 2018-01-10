package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.*;
import com.movilizer.mds.connector.MovilizerMetricService;

import java.util.*;

/**
 * The consolidation of the masterdata has to mirror the behavior expected in the MDS.
 *
 * This means that any delete masterdata is performed on the current cache but it also passed to the cloud since there's
 * no warranty that there was not an existing masterdata entry/reference in the cloud already.
 */
class MasterdataCache {

    private final MovilizerMetricService metrics;

    private Map<String, Map<String, MovilizerMasterdataUpdate>> updates = new HashMap<>();
    private Map<String, Set<String>> updateGroups = new HashMap<>();
    private Map<String, Map<String, MovilizerMasterdataReference>> references = new HashMap<>();
    private Map<String, Set<String>> referenceGroups = new HashMap<>();
    private Map<String, Map<String, MovilizerMasterdataDelete>> deletes = new HashMap<>();

    MasterdataCache(MovilizerMetricService metrics) {
        this.metrics = metrics;
    }

    /**
     * Applies the masterdata changes to the current cache.
     *
     * @param poolUpdate to be added to the cache
     */
    public void apply(MovilizerMasterdataPoolUpdate poolUpdate) {
        // The order is important as in the MDS the deletes are processed first
        poolUpdate.getDelete().forEach(delete -> apply(delete, poolUpdate.getPool()));
        poolUpdate.getUpdate().forEach(update -> apply(update, poolUpdate.getPool()));
        poolUpdate.getReference().forEach(reference -> apply(reference, poolUpdate.getPool()));
    }

    /**
     * Add current cache to the outbound request and clears the cache.
     *
     * @param request to add the movelets to
     * @return if there's a need to send
     */
    public boolean addToRequest(MovilizerRequest request) {
        List<MovilizerMasterdataPoolUpdate> poolUpdates = getPoolUpdates();
        if (!poolUpdates.isEmpty()) {
            request.getMasterdataPoolUpdate().addAll(poolUpdates);
            return true;
        }
        clear();
        return false;
    }

    private void apply(MovilizerMasterdataUpdate update, String poolName) {
        Map<String, MovilizerMasterdataUpdate> keyUpdateMap;
        if (updates.containsKey(poolName)) {
            keyUpdateMap = updates.get(poolName);
        } else {
            keyUpdateMap = new HashMap<>();
            updates.put(poolName, keyUpdateMap);
        }
        keyUpdateMap.put(update.getKey(), update);

        updateGroupCache(update.getKey(), update.getGroup(), poolName, updateGroups);
    }

    private void apply(MovilizerMasterdataReference reference, String poolName) {
        Map<String, MovilizerMasterdataReference> keyReferenceMap;
        if (references.containsKey(poolName)) {
            keyReferenceMap = references.get(poolName);
        } else {
            keyReferenceMap = new HashMap<>();
            references.put(poolName, keyReferenceMap);
        }
        keyReferenceMap.put(reference.getKey(), reference);

        updateGroupCache(reference.getKey(), reference.getGroup(), poolName, referenceGroups);
    }

    private void apply(MovilizerMasterdataDelete delete, String poolName) {
        Map<String, MovilizerMasterdataDelete> keyDeleteMap;
        if (deletes.containsKey(poolName)) {
            keyDeleteMap = deletes.get(poolName);
        } else {
            keyDeleteMap = new HashMap<>();
            deletes.put(poolName, keyDeleteMap);
        }
        keyDeleteMap.put(delete.getKey(), delete);
        cleanUpCache(delete, poolName);
    }

    private void updateGroupCache(String key, String group, String poolName, Map<String, Set<String>> cache) {
        if (group != null && !"".equals(group)) {
            Set<String> keysInGroup;
            String poolGroupKey = getPoolGroupKey(poolName, group);
            if (cache.containsKey(poolGroupKey)) {
                keysInGroup = cache.get(poolGroupKey);
            } else {
                keysInGroup = new HashSet<>();
                cache.put(poolGroupKey, keysInGroup);
            }
            keysInGroup.add(key);
        }
    }

    private String getPoolGroupKey(String poolName, String group) {
        return poolName + ":" + group;
    }

    private void cleanUpCache(MovilizerMasterdataDelete delete, String poolName) {
        if (isPoolDelete(delete)) {
            if (updates.containsKey(poolName)) {
                updates.remove(poolName);
            }
            if (references.containsKey(poolName)) {
                references.remove(poolName);
            }
        } else if (isGroupDelete(delete)) {
            String poolGroupKey = getPoolGroupKey(poolName, delete.getGroup());
            if (updates.containsKey(poolName) && updateGroups.containsKey(poolGroupKey)) {
                Map<String, MovilizerMasterdataUpdate> keyUpdateMap = updates.get(poolName);
                Set<String> keysInGroup = updateGroups.get(poolGroupKey);
                for (String key : keysInGroup) {
                    keyUpdateMap.remove(key);
                }
                if (keyUpdateMap.isEmpty()) {
                    updates.remove(poolName);
                }
                updateGroups.remove(poolGroupKey);
            }
            if (references.containsKey(poolName) && referenceGroups.containsKey(poolGroupKey)) {
                Map<String, MovilizerMasterdataReference> keyReferenceMap = references.get(poolName);
                Set<String> keysInGroup = referenceGroups.get(poolGroupKey);
                for (String key : keysInGroup) {
                    keyReferenceMap.remove(key);
                }
                if (keyReferenceMap.isEmpty()) {
                    references.remove(poolName);
                }
                referenceGroups.remove(poolGroupKey);
            }
        } else {
            // Just removing an entry
            if (updates.containsKey(poolName)) {
                Map<String, MovilizerMasterdataUpdate> keyUpdateMap = updates.get(poolName);
                if (keyUpdateMap.containsKey(delete.getKey())) {
                    keyUpdateMap.remove(delete.getKey());
                }
            }
            if (references.containsKey(poolName)) {
                Map<String, MovilizerMasterdataReference> keyReferenceMap = references.get(poolName);
                if (keyReferenceMap.containsKey(delete.getKey())) {
                    keyReferenceMap.remove(delete.getKey());
                }
            }
        }
    }

    private boolean isPoolDelete(MovilizerMasterdataDelete delete) {
        return isGroupDelete(delete) && (delete.getGroup() == null || "".equals(delete.getGroup()));
    }

    private boolean isGroupDelete(MovilizerMasterdataDelete delete) {
        return delete.getKey() == null || "".equals(delete.getKey());
    }

    private List<MovilizerMasterdataPoolUpdate> getPoolUpdates() {
        List<MovilizerMasterdataPoolUpdate> poolUpdates = new ArrayList<>();

        for (Map.Entry<String, Map<String, MovilizerMasterdataUpdate>> entry : updates.entrySet()) {
            MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
            poolUpdate.setPool(entry.getKey());
            for (MovilizerMasterdataUpdate update : entry.getValue().values()) {
                poolUpdate.getUpdate().add(update);
            }
            poolUpdates.add(poolUpdate);
        }

        for (Map.Entry<String, Map<String, MovilizerMasterdataReference>> entry : references.entrySet()) {
            MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
            poolUpdate.setPool(entry.getKey());
            for (MovilizerMasterdataReference reference : entry.getValue().values()) {
                poolUpdate.getReference().add(reference);
            }
            poolUpdates.add(poolUpdate);
        }

        for (Map.Entry<String, Map<String, MovilizerMasterdataDelete>> entry : deletes.entrySet()) {
            MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
            poolUpdate.setPool(entry.getKey());
            for (MovilizerMasterdataDelete delete : entry.getValue().values()) {
                poolUpdate.getDelete().add(delete);
            }
            poolUpdates.add(poolUpdate);
        }

        return poolUpdates;
    }

    /**
     * Number of masterdata changes in the current cache (mainly used for metrics).
     *
     * @return number of masterdata changes in cache
     */
    private Long size() {
        Long acc = 0L;
        for (Map<String, MovilizerMasterdataUpdate> map : updates.values()) {
            acc += map.size();
        }
        for (Map<String, MovilizerMasterdataDelete> map : deletes.values()) {
            acc += map.size();
        }
        for (Map<String, MovilizerMasterdataReference> map : references.values()) {
            acc += map.size();
        }
        return acc;
    }

    private void clear() {
        updates.clear();
        deletes.clear();
        references.clear();
        updateGroups.clear();
        referenceGroups.clear();
    }
}
