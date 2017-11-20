package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMovelet;
import com.movilitas.movilizer.v15.MovilizerMoveletDelete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoveletCache {

    private Map<String, MovilizerMovelet> moveletsExtended = new HashMap<>();
    private Map<String, Map<String, MovilizerMovelet>> movelets = new HashMap<>();
    private Map<String, MovilizerMoveletDelete> deletesExtended = new HashMap<>();
    private Map<String, Map<String, MovilizerMoveletDelete>> deletes = new HashMap<>();

    public List<MovilizerMovelet> getMovelets() {
        return movelets.values().stream()
                .flatMap(maps -> maps.values().stream())
                .collect(Collectors.toList());
    }

    public List<MovilizerMoveletDelete> getDeletes() {
        return deletes.values().stream()
                .flatMap(maps -> maps.values().stream())
                .collect(Collectors.toList());
    }

    public void apply(MovilizerMovelet movelet) {
        String key = getKeyExtended(movelet);
        if (moveletsExtended.containsKey(key)) {
            if (moveletsExtended.get(key).getMoveletVersion() < movelet.getMoveletVersion()) {
                moveletsExtended.put(key, movelet);
                movelets.get(movelet.getMoveletKey()).put(movelet.getMoveletKeyExtension(), movelet);
            }
        } else {
            moveletsExtended.put(key, movelet);
            if (movelets.containsKey(movelet.getMoveletKey())) {
                movelets.get(movelet.getMoveletKey()).put(movelet.getMoveletKeyExtension(), movelet);
            } else {
                Map<String, MovilizerMovelet> cache = new HashMap<>();
                cache.put(movelet.getMoveletKeyExtension(), movelet);
                movelets.put(movelet.getMoveletKey(), cache);
            }
        }
    }

    public void apply(MovilizerMoveletDelete delete) {
        String key = getKeyExtended(delete);

        if (!deletes.containsKey(delete.getMoveletKey())) {
            deletes.put(delete.getMoveletKey(), new HashMap<>());
        }

        if (delete.isIgnoreExtensionKey()) {
            // Remove all specific entries for the extended keys
            deletes.get(delete.getMoveletKey()).forEach((keyExt, deleteExt) ->
                    deletesExtended.remove(getKeyExtended(deleteExt)));
            deletes.get(delete.getMoveletKey()).clear();
            // Apply to movelets too
            if (movelets.containsKey(delete.getMoveletKey())) {
                movelets.get(delete.getMoveletKey()).forEach((keyExt, movelet) ->
                        moveletsExtended.remove(getKeyExtended(movelet)));
                movelets.remove(delete.getMoveletKey());
            }
        }

        if (moveletsExtended.containsKey(key)) {
            moveletsExtended.remove(key);
        }

        String ext = delete.getMoveletKeyExtension() != null ? delete.getMoveletKeyExtension() : "";
        deletes.get(delete.getMoveletKey()).put(ext, delete);
        deletesExtended.put(key, delete);
    }

    private String getKeyExtended(MovilizerMovelet movelet) {
        return movelet.getMoveletKey() + movelet.getMoveletKeyExtension();
    }

    private String getKeyExtended(MovilizerMoveletDelete delete) {
        if (delete.isIgnoreExtensionKey()) {
            return delete.getMoveletKey();
        }
        return delete.getMoveletKey() + delete.getMoveletKeyExtension();
    }
}
