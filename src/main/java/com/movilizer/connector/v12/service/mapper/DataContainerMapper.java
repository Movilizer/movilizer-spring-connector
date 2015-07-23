package com.movilizer.connector.v12.service.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movilitas.movilizer.v14.MovilizerGenericDataContainer;
import com.movilitas.movilizer.v14.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.v12.exception.MovilizerParsingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class maps DataContainer Arrays to DomainObjects.<br />
 * <p>
 * This solution can handle ArrayLists but is not able to handle HashMaps. Objects of a list have
 * numerical keys.<br />
 * <p>
 * fooList : {0 : {BarObject}; 1 : {BarObject}}<br />
 * barObject : {field1Name : "value"; field2Name : "value"}<br />
 * <p>
 * Its not possible to place Lists on the TopLevel. It must be an Object for the moment.<br />
 *
 * @author Pavel Kotlov
 */
@Service
public class DataContainerMapper {

    private static Log logger = LogFactory.getLog(DataContainerMapper.class);

    public Object toObject(MovilizerUploadDataContainer dataContainer) {
        String className = getClassName(dataContainer);
        try {
            Class<?> classType = Class.forName(className);
            return toObject(dataContainer.getContainer().getData(), classType);
        } catch (ClassNotFoundException e) {
            throw new MovilizerParsingException("The Class was not on the Classpath.", e);
        }
    }

    public boolean checkForClass(MovilizerUploadDataContainer dataContainer, Class<?>... classes) {
        for (Class<?> currentClass : classes) {
            String className = getClassName(dataContainer);
            if (currentClass.getName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    private String getClassName(MovilizerUploadDataContainer dataContainer) {
        String className = dataContainer.getContainer().getKey();
        for (String currentToken : className.split(":")) {
            return currentToken;
        }
        return className;
    }

    public <T> T toObject(MovilizerGenericDataContainer containerData, Class<T> toValueType) {
        Map<String, Object> objectAsMap = new HashMap<String, Object>();
        if (containerData != null) {
            objectAsMap = getMap(containerData.getEntry());
        }
        try {
            return new ObjectMapper().convertValue(objectAsMap, toValueType);
        } catch (IllegalArgumentException e) {
            throw new MovilizerParsingException("Error parsing the DataConatiner with a JSON Parser. " +
                    toValueType.getName(), e);
        }
    }

    private Map<String, Object> getMap(List<MovilizerGenericDataContainerEntry> entryList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
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
        List<Object> resultList = new ArrayList<Object>();
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
                if (isInteger(entry.getName()) == false) {
                    throw new MovilizerParsingException("The entry [" + entry.getName() +
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
