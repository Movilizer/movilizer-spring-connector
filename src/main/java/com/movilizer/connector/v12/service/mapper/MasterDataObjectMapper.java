/*
 *
 * Copyright (c) 2012-2015 Movilizer GmbH,
 * Julius-Hatry-Straße 1, D-68163 Mannheim GmbH, Germany.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Movilizer GmbH ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Movilizer.
 */

package com.movilizer.connector.v12.service.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movilitas.movilizer.v12.MovilizerGenericDataContainerEntry;

import java.util.List;
import java.util.Map;

public class MasterDataObjectMapper {

    public MovilizerGenericDataContainerEntry objectToContainer(Object object) {
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

    public void addSybEntry(MovilizerGenericDataContainerEntry entry, String key, Object value) {
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
