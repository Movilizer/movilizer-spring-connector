package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterdataCache {

    private Map<String, MovilizerMasterdataUpdate> updatesExtended = new HashMap<>();
    private Map<String, Map<String, MovilizerMasterdataUpdate>> updates = new HashMap<>();
    private Map<String, MovilizerMasterdataReference> referencesExtended = new HashMap<>();
    private Map<String, Map<String, MovilizerMasterdataReference>> references = new HashMap<>();
    private Map<String, MovilizerMasterdataDelete> deletesExtended = new HashMap<>();
    private Map<String, Map<String, MovilizerMasterdataDelete>> deletes = new HashMap<>();

    public List<MovilizerMasterdataPoolUpdate> getPoolUpdates() {
        List<MovilizerMasterdataPoolUpdate> poolUpdates = new ArrayList<>();
//        for (Map.Entry<String, Map<String, MovilizerMasterdataUpdate>> entry : updates.entrySet()) {
//            MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
//            poolUpdate.setPool(entry.getKey());
//            for (MovilizerMasterdataUpdate update : entry.getValue().values()) {
//                poolUpdate.getUpdate().add(update);
//            }
//        }
//        for (Map.Entry<String, Map<String, MovilizerMasterdataDelete>> entry : deletes.entrySet()) {
//            MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
//            poolUpdate.setPool(entry.getKey());
//            for (MovilizerMasterdataDelete delete : entry.getValue().values()) {
//                poolUpdate.getDelete().add(delete);
//            }
//        }
//        for (Map.Entry<String, Map<String, MovilizerMasterdataReference>> entry : references.entrySet()) {
//            MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
//            poolUpdate.setPool(entry.getKey());
//            for (MovilizerMasterdataReference reference : entry.getValue().values()) {
//                poolUpdate.getReference().add(reference);
//            }
//        }
        return poolUpdates;
    }

    public void apply(MovilizerMasterdataPoolUpdate poolUpdate) {
        poolUpdate.getDelete().forEach(delete -> apply(delete, poolUpdate.getPool()));
        poolUpdate.getUpdate().forEach(update -> apply(update, poolUpdate.getPool()));
        poolUpdate.getReference().forEach(reference -> apply(reference, poolUpdate.getPool()));
    }

    public void apply(MovilizerMasterdataUpdate update, String poolName) {

    }

    public void apply(MovilizerMasterdataReference reference, String poolName) {

    }

    private void apply(MovilizerMasterdataDelete delete, String poolName) {

    }

    private String getKeyExtended(MovilizerMasterdataUpdate update, String poolName) {
        return poolName + ":" + update.getGroup() + ":" + update.getKey();
    }

    private String getKeyExtended(MovilizerMasterdataDelete delete, String poolName) {
        return poolName + ":" + delete.getGroup() + ":" + delete.getKey();
    }

    private String getKeyExtended(MovilizerMasterdataReference reference, String poolName) {
        return poolName + ":" + reference.getGroup() + ":" + reference.getKey();
    }

    private boolean isPoolDelete(MovilizerMasterdataDelete delete) {
        return false;
    }

    private boolean isGroupDelete(MovilizerMasterdataDelete delete) {
        return false;
    }
}
