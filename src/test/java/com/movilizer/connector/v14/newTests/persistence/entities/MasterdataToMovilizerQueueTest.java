package com.movilizer.connector.v14.newTests.persistence.entities;

import com.movilitas.movilizer.v14.*;
import com.movilizer.connector.java.persistence.entities.MasterdataToMovilizerQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class MasterdataToMovilizerQueueTest {
    //Test data
    private MasterdataToMovilizerQueue masterdataToMovilizerQueue;
    private MovilizerMasterdataDelete masterdataDelete;
    private MovilizerMasterdataUpdate masterdataUpdate;
    private MovilizerMasterdataReference masterdataReference;

    @Before
    public void before() {
        MovilizerGenericDataContainer objectContainer = new MovilizerGenericDataContainer();
        objectContainer.getEntry().add(createDataEntry("intField", "1"));
        objectContainer.getEntry().add(createDataEntry("booleanField", "true"));
        objectContainer.getEntry().add(createDataEntry("dateField", "2015-08-30"));
        objectContainer.getEntry().add(createDataEntry("stringField", "test"));
        objectContainer.getEntry().add(createListEntry());
        objectContainer.getEntry().add(createObjectEntry());

        masterdataUpdate = new MovilizerMasterdataUpdate();
        masterdataUpdate.setData(objectContainer);
        masterdataUpdate.setKey("masterdataKey");
        masterdataUpdate.setDescription("masterdataDescription");
        masterdataUpdate.setFilter1("filter1");

        masterdataDelete = new MovilizerMasterdataDelete();
        masterdataDelete.setGroup("masterdataGroup");
        masterdataDelete.setKey("masterdataKey");

        masterdataReference = new MovilizerMasterdataReference();
        masterdataReference.setGroup("masterdataGroup");
        masterdataReference.setKey("masterdataKey");
    }

    @After
    public void after() {
    }

    @Test
    public void testMasterdataToMovilizerQueueMasterdataUpdateConstructor() throws Exception {
        masterdataToMovilizerQueue = new MasterdataToMovilizerQueue("pool", masterdataUpdate);

        assertThat(masterdataToMovilizerQueue.getMasterdataUpdate(), is(masterdataUpdate));
        assertThat(masterdataToMovilizerQueue.getKey(), is(masterdataUpdate.getKey()));
        assertThat(masterdataToMovilizerQueue.getDescription(), is(masterdataUpdate.getDescription()));
        assertThat(masterdataToMovilizerQueue.getFilter1(), is(masterdataUpdate.getFilter1()));
    }

    @Test
    public void testMasterdataToMovilizerQueueMasterdataDeleteConstructor() throws Exception {
        masterdataToMovilizerQueue = new MasterdataToMovilizerQueue("pool", masterdataDelete);

        assertThat(masterdataToMovilizerQueue.getMasterdataDelete(), is(masterdataDelete));
        assertThat(masterdataToMovilizerQueue.getKey(), is(masterdataDelete.getKey()));
        assertThat(masterdataToMovilizerQueue.getGroup(), is(masterdataDelete.getGroup()));
    }

    @Test
    public void testMasterdataToMovilizerQueueMasterdataReferenceConstructor() throws Exception {
        masterdataToMovilizerQueue = new MasterdataToMovilizerQueue("pool", masterdataReference);

        assertThat(masterdataToMovilizerQueue.getMasterdataReference(), is(masterdataReference));
        assertThat(masterdataToMovilizerQueue.getKey(), is(masterdataReference.getKey()));
        assertThat(masterdataToMovilizerQueue.getGroup(), is(masterdataReference.getGroup()));
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
