package com.movilizer.connector.newTests.persistence.entities;

import com.movilitas.movilizer.v15.MovilizerGenericDataContainer;
import com.movilitas.movilizer.v15.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v15.MovilizerGenericUploadDataContainer;
import com.movilitas.movilizer.v15.MovilizerUploadDataContainer;
import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class DatacontainerFromMovilizerQueueTest {
    //Test data
    private DatacontainerFromMovilizerQueue datacontainerFromMovilizerQueue;
    private MovilizerUploadDataContainer dataContainer;

    @Before
    public void before() {
        MovilizerGenericDataContainer objectContainer = new MovilizerGenericDataContainer();
        objectContainer.getEntry().add(createDataEntry("intField", "1"));
        objectContainer.getEntry().add(createDataEntry("booleanField", "true"));
        objectContainer.getEntry().add(createDataEntry("dateField", "2015-08-30"));
        objectContainer.getEntry().add(createDataEntry("stringField", "test"));
        objectContainer.getEntry().add(createListEntry());
        objectContainer.getEntry().add(createObjectEntry());
        MovilizerGenericUploadDataContainer genericContainer = new MovilizerGenericUploadDataContainer();
        genericContainer.setKey("containerKey");
        genericContainer.setDeviceAddress("deviceAddress");
        genericContainer.setData(objectContainer);
        dataContainer = new MovilizerUploadDataContainer();
        dataContainer.setContainer(genericContainer);

    }

    @After
    public void after() {
    }

    @Test
    public void testDatacontainerFromMovilizerQueueConstructor() throws Exception {
        datacontainerFromMovilizerQueue = new DatacontainerFromMovilizerQueue(dataContainer);

        assertThat(datacontainerFromMovilizerQueue.getDatacontainer(), is(dataContainer));
        assertThat(datacontainerFromMovilizerQueue.getKey(), is(dataContainer.getContainer().getKey()));
        assertThat(datacontainerFromMovilizerQueue.getDeviceAddress(), is(dataContainer.getContainer().getDeviceAddress()));
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

}
