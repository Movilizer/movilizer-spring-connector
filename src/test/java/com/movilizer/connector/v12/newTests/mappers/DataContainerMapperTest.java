package com.movilizer.connector.v12.newTests.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movilitas.movilizer.v12.MovilizerGenericDataContainer;
import com.movilitas.movilizer.v12.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v12.MovilizerGenericUploadDataContainer;
import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
import com.movilizer.connector.java.mapper.direct.GenericDataContainerMapperImpl;
import com.movilizer.connector.v12.newTests.mappers.models.MapperTestObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DataContainerMapperTest {

    private GenericDataContainerMapperImpl dcMapper;

    @Before
    public void before() {
        dcMapper = new GenericDataContainerMapperImpl();
    }

    @After
    public void after() {
    }

    @Test
    public void convertObjectToMap() {
        ObjectMapper objectMapper = new ObjectMapper();

        @SuppressWarnings("unchecked")
        Map<String, Object> objectAsMap = objectMapper.convertValue(MapperTestObject.createTestObject(), Map.class);

        assertThat(objectAsMap, hasEntry("booleanField", (Object) true));
        assertThat(objectAsMap, hasEntry("dateField", "2015-01-01"));
        assertThat((Map<String, Object>) objectAsMap.get("objectField"), hasEntry("stringField", (Object) "test"));

    }

    @Test
    public void convertMapToObject() {
        Map<String, Object> objectAsMap = new HashMap<>();
        objectAsMap.put("booleanField", "true");
        objectAsMap.put("stringField", "test");
        Map<String, Object> subObjectAsMap = new HashMap<>();
        subObjectAsMap.put("stringField", "test");
        objectAsMap.put("objectField", subObjectAsMap);

        List<Map<String, Object>> objectFieldList = new ArrayList<>();
        Map<String, Object> subObjectAsMapForList = new HashMap<>();
        subObjectAsMapForList.put("stringField", "test");
        objectFieldList.add(subObjectAsMapForList);
        objectAsMap.put("objectFieldList", objectFieldList);

        ObjectMapper objectMapper = new ObjectMapper();
        MapperTestObject tMapperTestObject = objectMapper.convertValue(objectAsMap, MapperTestObject.class);

        assertThat(tMapperTestObject.isBooleanField(), is(equalTo(true)));
        assertThat(tMapperTestObject.getStringField(), is(equalTo("test")));
        assertThat(tMapperTestObject.getObjectField().getStringField(), is(equalTo("test")));
        assertThat(tMapperTestObject.getObjectFieldList().get(0).getStringField(), is(equalTo("test")));
    }

    @Test
    public void convertObjectToDC() throws ClassNotFoundException {
        MovilizerGenericDataContainer dataContainer = dcMapper.toDataContainer(MapperTestObject.createTestObject());

        assertThat(getEntry(dataContainer.getEntry(), "stringField").getValstr(), is("test"));
        assertThat(getEntry(dataContainer.getEntry(), "intField").getValstr(), is("100"));
        assertThat(getEntry(dataContainer.getEntry(), "booleanField").getValstr(), is("true"));
        assertThat(getEntry(dataContainer.getEntry(), "dateField").getValstr(), is("2015-01-01"));

        MovilizerGenericDataContainerEntry objectField = getEntry(dataContainer.getEntry(), "objectField");
        assertThat(getEntry(objectField.getEntry(), "stringField").getValstr(), is("test"));

        MovilizerGenericDataContainerEntry objectFieldList = getEntry(dataContainer.getEntry(), "objectFieldList");
        MovilizerGenericDataContainerEntry objectFieldListFirstEntry = objectFieldList.getEntry().get(0);
        assertThat(getEntry(objectFieldListFirstEntry.getEntry(), "stringField").getValstr(), is("test"));
    }


    @Test
    public void convertDCtoObject() throws ClassNotFoundException {
        MovilizerUploadDataContainer container = new MovilizerUploadDataContainer();
        MovilizerGenericUploadDataContainer genericContainer = new MovilizerGenericUploadDataContainer();
        container.setContainer(genericContainer);

        genericContainer.setKey("containerKey");

        MovilizerGenericDataContainer objectContainer = new MovilizerGenericDataContainer();
        objectContainer.getEntry().add(createDataEntry(GenericDataContainerMapperImpl.JAVA_CLASS_ENTRY, "com.movilizer.connector.v12.newTests.mappers.models.MapperTestObject"));
        objectContainer.getEntry().add(createDataEntry("intField", "1"));
        objectContainer.getEntry().add(createDataEntry("booleanField", "true"));
        objectContainer.getEntry().add(createDataEntry("dateField", "2015-08-30"));
        objectContainer.getEntry().add(createDataEntry("stringField", "test"));
        objectContainer.getEntry().add(createListEntry());
        objectContainer.getEntry().add(createObjectEntry());
        genericContainer.setData(objectContainer);

        assertThat(dcMapper.isType(container.getContainer().getData(), MapperTestObject.class), is(equalTo(true)));
        assertThat(dcMapper.getType(container.getContainer().getData()).equals(MapperTestObject.class), is(equalTo(true)));

        MapperTestObject tMapperTestObject = dcMapper.fromDataContainer(container.getContainer().getData(), MapperTestObject.class);

        assertThat(tMapperTestObject.isBooleanField(), is(equalTo(true)));
        assertThat(tMapperTestObject.getStringField(), is(equalTo("test")));
        assertThat(tMapperTestObject.getObjectField().getStringField(), is(equalTo("test")));
        assertThat(tMapperTestObject.getObjectFieldList().get(0).getStringField(), is(equalTo("test")));
    }

    private MovilizerGenericDataContainerEntry createDataEntry(String key, String value) {
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName(key);
        entry.setValstr(value);
        return entry;
    }

    private MovilizerGenericDataContainerEntry createListEntry() {
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName("objectFieldList");
        entry.getEntry().add(createObjectListEntry(0));
        return entry;
    }

    private MovilizerGenericDataContainerEntry createObjectEntry() {
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName("objectField");
        entry.getEntry().add(createDataEntry("stringField", "test"));
        return entry;
    }

    private MovilizerGenericDataContainerEntry createObjectListEntry(int key) {
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName(Integer.toString(key));
        entry.getEntry().add(createDataEntry("stringField", "test"));
        return entry;
    }

    private MovilizerGenericDataContainerEntry getEntry(List<MovilizerGenericDataContainerEntry> updates, String key) {
        for (MovilizerGenericDataContainerEntry entry : updates) {
            if (entry.getName().equals(key)) {
                return entry;
            }
        }
        return null;
    }
}
