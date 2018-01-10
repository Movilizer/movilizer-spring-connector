package com.movilizer.mds.connector.mapper.masterdata;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movilitas.movilizer.v15.MovilizerGenericDataContainerEntry;

import java.util.List;
import java.util.Map;

class MasterDataObjectMapper {

    MovilizerGenericDataContainerEntry objectToContainer(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        if (object != null) {
            if (object instanceof List) {
                int i = 0;
                for (Object currentObject : (List<Object>) object) {
                    MovilizerGenericDataContainerEntry subEntry = objectToContainer(currentObject);
                    subEntry.setName(Integer.toString(i++));
                    entry.getEntry().add(subEntry);
                }
            } else {
                @SuppressWarnings("unchecked")
                Map<String, Object> objectAsMap = objectMapper.convertValue(object, Map.class);
                entry = mapToDataContainerEntry(objectAsMap);
            }
        }
        return entry;
    }

    private MovilizerGenericDataContainerEntry mapToDataContainerEntry(Map<String, Object> objectAsMap) {
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        for (String key : objectAsMap.keySet()) {
            Object value = objectAsMap.get(key);
            addSybEntry(entry, key, value);
        }
        return entry;
    }

    private void addSybEntry(MovilizerGenericDataContainerEntry entry, String key, Object value) {
        if (value == null) {
            return;
        }
        MovilizerGenericDataContainerEntry subEntry = new MovilizerGenericDataContainerEntry();
        if (value instanceof Map) {
            subEntry = mapToDataContainerEntry((Map<String, Object>) value);
        } else if (value instanceof List) {
            subEntry = listToDataContainerEntry((List<Object>) value);
        } else {
            subEntry.setValstr(value.toString());
        }
        subEntry.setName(key);
        entry.getEntry().add(subEntry);
    }

    private MovilizerGenericDataContainerEntry listToDataContainerEntry(List<Object> list) {
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        int i = 0;
        for (Object value : list) {
            addSybEntry(entry, Integer.toString(i++), value);
        }
        return entry;
    }
}
