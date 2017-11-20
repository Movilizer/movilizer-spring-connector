package com.movilizer.mds.connector.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movilitas.movilizer.v15.MovilizerGenericDataContainer;
import com.movilitas.movilizer.v15.MovilizerGenericDataContainerEntry;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.exceptions.MovilizerMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class maps DataContainer Arrays to DomainObjects.
 * <p>
 * This solution can handle ArrayLists but is not able to handle HashMaps. Objects of a list have
 * numerical keys.<br />
 * <p>
 * fooList : {0 : {BarObject}; 1 : {BarObject}}
 * barObject : {field1Name : "value"; field2Name : "value"}
 * <p>
 * Its not possible to place Lists on the TopLevel. It must be an Object for the moment.
 */
@Component
public class GenericDataContainerMapperImpl implements GenericDataContainerMapper {
    private static final Logger logger = LoggerFactory.getLogger(GenericDataContainerMapperImpl.class);

    public static final String JAVA_CLASS_ENTRY = "_class";

    private final MovilizerMetricService metricService;

    @Autowired
    public GenericDataContainerMapperImpl(MovilizerMetricService metricService) {
        this.metricService = metricService;
    }

    @Override
    public <T> T fromDataContainer(MovilizerGenericDataContainer containerData, Class<T> toValueType) {
        long start = System.nanoTime();
        Map<String, Object> objectAsMap = new HashMap<>();
        if (containerData != null) {
            objectAsMap = getMap(containerData.getEntry());
            objectAsMap.remove(JAVA_CLASS_ENTRY);
        }
        try {
            T object = new ObjectMapper().convertValue(objectAsMap, toValueType);
            long time = System.nanoTime() - start;
            metricService.mapperFromTimeSubmit(time);
            if (logger.isTraceEnabled()) {
                logger.trace("Mapped from " + containerData.toString() + " in " + time + " ns");
            }
            return object;
        } catch (IllegalArgumentException e) {
            throw new MovilizerMappingException("Error parsing the DataContainer with a JSON Parser. " +
                    toValueType.getName(), e);
        }
    }

    @Override
    public <T> boolean isType(MovilizerGenericDataContainer dataContainer, Class<T> objectType) throws MovilizerMappingException {
        boolean isType = false;
        String className = getTypeCanonicalName(dataContainer);
        if (objectType.getName().equals(className)) {
            isType = true;
        }
        return isType;
    }

    @Override
    public <T> Class<T> getType(MovilizerGenericDataContainer dataContainer) throws ClassNotFoundException {
        String className = getTypeCanonicalName(dataContainer);
        Class<T> classType = (Class<T>) Class.forName(className);
        return classType;
    }

    @Override
    public String getTypeCanonicalName(MovilizerGenericDataContainer dataContainer) throws MovilizerMappingException {
        for (MovilizerGenericDataContainerEntry entry : dataContainer.getEntry()) {
            if (entry.getName().equals(JAVA_CLASS_ENTRY)) {
                return entry.getValstr();
            }
        }
        throw new MovilizerMappingException("Java class entry (\"" + JAVA_CLASS_ENTRY + "\") not found in dataContainer");
    }

    @Override
    public <T> MovilizerGenericDataContainer toDataContainer(T instanceOfObject) {
        long start = System.nanoTime();
        MovilizerGenericDataContainer dataContainer = new MovilizerGenericDataContainer();
        if (instanceOfObject != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> objectAsMap = objectMapper.convertValue(instanceOfObject, Map.class);
            dataContainer.getEntry().addAll(mapToDc(objectAsMap));
        }
        long time = System.nanoTime() - start;
        metricService.mapperToTimeSubmit(time);
        if (logger.isTraceEnabled()) {
            logger.trace("Mapped " + instanceOfObject.toString() + "to datacontainer in " + time + " ns");
        }
        return dataContainer;
    }

    @Override
    public <T> MovilizerGenericDataContainer toDataContainer(List<T> listOfObjects) {
        long start = System.nanoTime();
        MovilizerGenericDataContainer dataContainer = new MovilizerGenericDataContainer();
        for (T instanceOfObject : listOfObjects) {
            MovilizerGenericDataContainerEntry entries = new MovilizerGenericDataContainerEntry();
            entries.getEntry().addAll(toDataContainer(instanceOfObject).getEntry());
            dataContainer.getEntry().add(entries);
        }
        long time = System.nanoTime() - start;
        metricService.mapperToTimeSubmit(time);
        if (logger.isTraceEnabled()) {
            logger.trace("Mapped " + listOfObjects.toString() + "to datacontainer in " + time + " ns");
        }
        return dataContainer;
    }

    private List<MovilizerGenericDataContainerEntry> mapToDc(Map<String, Object> objectAsMap) {
        List<MovilizerGenericDataContainerEntry> movilizerGenericDataContainer = new ArrayList<>();
        for (Map.Entry<String, Object> entry : objectAsMap.entrySet()) {
            movilizerGenericDataContainer.add(mapToDcAux(entry.getKey(), entry.getValue()));
        }
        return movilizerGenericDataContainer;
    }

    private MovilizerGenericDataContainerEntry mapToDcAux(String key, Object value) {
        MovilizerGenericDataContainerEntry entries = new MovilizerGenericDataContainerEntry();
        entries.setName(key);
        if (value instanceof byte[]) {
            entries.setValb64((byte[]) value);
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
                MovilizerGenericDataContainerEntry entryToAdd = mapToDcAux(mapEntry.getKey(), mapEntry.getValue());
                entries.getEntry().add(entryToAdd);
            }
        } else if (value instanceof List) {
            List list = (List) value;
            for (int i = 0; i < list.size(); i++) {
                MovilizerGenericDataContainerEntry entryToAdd = mapToDcAux(String.valueOf(i), list.get(i));
                entries.getEntry().add(entryToAdd);
            }
        } else {
            if (value != null) {
                entries.setValstr(String.valueOf(value));
            } else {
                entries.setValstr("");
            }
        }
        return entries;
    }

    private Map<String, Object> getMap(List<MovilizerGenericDataContainerEntry> entryList) {
        Map<String, Object> resultMap = new HashMap<>();
        for (MovilizerGenericDataContainerEntry entry : entryList) {
            String key = entry.getName();
            resultMap.put(key, getValue(entry));
        }
        return resultMap;
    }

    private Object getMapOrList(List<MovilizerGenericDataContainerEntry> entryList) {
        if (isList(entryList)) {
            return getList(entryList);
        } else {
            return getMap(entryList);
        }
    }

    private List<Object> getList(List<MovilizerGenericDataContainerEntry> entryList) {
        List<Object> resultList = new ArrayList<>();
        for (MovilizerGenericDataContainerEntry entry : entryList) {
            resultList.add(getValue(entry));
        }
        return resultList;
    }

    private Object getValue(MovilizerGenericDataContainerEntry entry) {
        Object value = null;
        List<MovilizerGenericDataContainerEntry> currentEntryList = entry.getEntry();
        if (currentEntryList != null && currentEntryList.size() > 0) {
            value = getMapOrList(currentEntryList);
        } else if (entry.getValstr() != null) {
            value = entry.getValstr();
        } else if (entry.getValb64() != null) {
            value = entry.getValb64();
        }
        return value;
    }

    private boolean isList(List<MovilizerGenericDataContainerEntry> entryList) {
        if (isInteger(entryList.get(0).getName())) {
            for (MovilizerGenericDataContainerEntry entry : entryList) {
                if (!isInteger(entry.getName())) {
                    throw new MovilizerMappingException("The entry [" + entry.getName() +
                            "] was not an integer although a List structure was anticipated " +
                            "because of the first element of the Data Structure [" + entryList.get(0).getName() +
                            "].");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Integer.parseInt(int i); is 3-4 times slower.
     *
     * @param str
     * @return
     */
    private boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }
}
