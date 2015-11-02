package com.movilizer.connector.v12.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movilitas.movilizer.v12.MovilizerGenericDataContainer;
import com.movilitas.movilizer.v12.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v12.MovilizerGenericUploadDataContainer;
import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
import com.movilizer.connector.java.mapper.direct.GenericDataContainerMapperImpl;
import com.movilizer.connector.v12.mapper.models.MapperTestObjectDC;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DataContainerMapperTest {

//    DataContainerMapper mapper = new DataContainerMapper();
//
//    @Test
//    public void convertObjectToMap() {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        @SuppressWarnings("unchecked")
//        Map<String, Object> objectAsMap = objectMapper.convertValue(
//                MapperTestObjectDC.createTestObject(), Map.class);
//
//        assertThat(objectAsMap, hasEntry("booleanField", (Object) true));
//        assertThat(objectAsMap, hasEntry("dateField", "2015-01-01"));
//        assertThat((Map<String, Object>) objectAsMap.get("objectField"),
//                hasEntry("stringField", (Object) "test"));
//    }
//
//    @Test
//    public void convertMapToObject() {
//        Map<String, Object> objectAsMap = new HashMap<String, Object>();
//        objectAsMap.put("booleanField", "true");
//        objectAsMap.put("stringField", "test");
//        Map<String, Object> subObjectAsMap = new HashMap<String, Object>();
//        subObjectAsMap.put("stringField", "test");
//        objectAsMap.put("objectField", subObjectAsMap);
//
//        List<Map<String, Object>> objectFieldList = new ArrayList<Map<String, Object>>();
//        Map<String, Object> subObjectAsMapForList = new HashMap<String, Object>();
//        subObjectAsMapForList.put("stringField", "test");
//        objectFieldList.add(subObjectAsMapForList);
//        objectAsMap.put("objectFieldList", objectFieldList);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        MapperTestObjectDC tMapperTestObjectDC = objectMapper.convertValue(objectAsMap,
//                MapperTestObjectDC.class);
//
//        assertThat(tMapperTestObjectDC.isBooleanField(), is(equalTo(true)));
//        assertThat(tMapperTestObjectDC.getStringField(), is(equalTo("test")));
//        assertThat(tMapperTestObjectDC.getObjectField().getStringField(), is(equalTo("test")));
//        assertThat(tMapperTestObjectDC.getObjectFieldList().get(0).getStringField(),
//                is(equalTo("test")));
//    }
//
//    @Test
//    public void convertDCtoObject() {
//        MovilizerUploadDataContainer container = new MovilizerUploadDataContainer();
//        MovilizerGenericUploadDataContainer genericContainer = new MovilizerGenericUploadDataContainer();
//        container.setContainer(genericContainer);
//
//        genericContainer.setKey("com.movilizer.modules.movilizer.v12.mapper.models.MapperTestObjectDC:001");
//
//        MovilizerGenericDataContainer objectContainer = new MovilizerGenericDataContainer();
//        objectContainer.getEntry().add(createDataEntry("booleanField", "true"));
//        objectContainer.getEntry().add(createListEntry());
//        objectContainer.getEntry().add(createDataEntry("stringField", "test"));
//        objectContainer.getEntry().add(createObjectEntry());
//
//        genericContainer.setData(objectContainer);
//
//        MapperTestObjectDC tMapperTestObjectDC = (MapperTestObjectDC) mapper.toObject(container);
//
//        assertThat(tMapperTestObjectDC.isBooleanField(), is(equalTo(true)));
//        assertThat(tMapperTestObjectDC.getStringField(), is(equalTo("test")));
//        assertThat(tMapperTestObjectDC.getObjectField().getStringField(), is(equalTo("test")));
//        assertThat(tMapperTestObjectDC.getObjectFieldList().get(0).getStringField(),
//                is(equalTo("test")));
//    }
//
//    private MovilizerGenericDataContainerEntry createDataEntry(String key, String value) {
//        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
//        entry.setName(key);
//        entry.setValstr(value);
//        return entry;
//    }
//
//    private MovilizerGenericDataContainerEntry createListEntry() {
//        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
//        entry.setName("objectFieldList");
//        entry.getEntry().add(createObjectListEntry(0));
//        return entry;
//    }
//
//    private MovilizerGenericDataContainerEntry createObjectEntry() {
//        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
//        entry.setName("objectField");
//        entry.getEntry().add(createDataEntry("stringField", "test"));
//        return entry;
//    }
//
//    private MovilizerGenericDataContainerEntry createObjectListEntry(int key) {
//        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
//        entry.setName(Integer.toString(key));
//        entry.getEntry().add(createDataEntry("stringField", "test"));
//        return entry;
//    }
}
