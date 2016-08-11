package com.movilizer.connector.newTests.persistence.entities;

import com.movilitas.movilizer.v14.*;
import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.persistence.entities.ParticipantToMovilizerQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class ParticipantToMovilizerQueueTest {
    //Test data
    private ParticipantToMovilizerQueue participantToMovilizerQueue;
    private MovilizerParticipantReset participantReset;
    private MovilizerMoveletAssignment moveletAssignment;
    private MovilizerMoveletAssignmentDelete moveletAssignmentDelete;
    private MovilizerParticipantConfiguration participantConfiguration;

    @Before
    public void before() {
        participantReset = new MovilizerParticipantReset();
        participantReset.setDeviceAddress("deviceAddress");

        moveletAssignment = new MovilizerMoveletAssignment();
        moveletAssignment.setMoveletKey("moveletKey");
        moveletAssignment.setMoveletKeyExtension("moveletKeyExtension");

        moveletAssignmentDelete = new MovilizerMoveletAssignmentDelete();
        moveletAssignmentDelete.setDeviceAddress("deviceAddress");
        moveletAssignmentDelete.setMoveletKey("moveletKey");
        moveletAssignmentDelete.setMoveletKeyExtension("moveletKeyExtension");

        participantConfiguration = new MovilizerParticipantConfiguration();
        participantConfiguration.setDeviceAddress("deviceAddress");
        participantConfiguration.setName("name");
        participantConfiguration.setPasswordHashType(0);
    }

    @After
    public void after() {
    }

    @Test
    public void testParticipantToMovilizerQueueParticipantResetConstructor() throws Exception {
        participantToMovilizerQueue = new ParticipantToMovilizerQueue(participantReset);

        assertThat(participantToMovilizerQueue.getParticipantReset(), is(participantReset));
        assertThat(participantToMovilizerQueue.getDeviceAddress(), is(participantReset.getDeviceAddress()));
    }

    @Test
    public void testParticipantToMovilizerQueueMoveletAssignmentConstructor() throws Exception {
        participantToMovilizerQueue = new ParticipantToMovilizerQueue(moveletAssignment);

        assertThat(participantToMovilizerQueue.getAssignment(), is(moveletAssignment));
        assertThat(participantToMovilizerQueue.getMoveletKey(), is(moveletAssignment.getMoveletKey()));
        assertThat(participantToMovilizerQueue.getMoveletKeyExtension(), is(moveletAssignment.getMoveletKeyExtension()));
    }

    @Test
    public void testParticipantToMovilizerQueueMoveletAssignmentDeleteConstructor() throws Exception {
        participantToMovilizerQueue = new ParticipantToMovilizerQueue(moveletAssignmentDelete);

        assertThat(participantToMovilizerQueue.getAssignmentDelete(), is(moveletAssignmentDelete));
        assertThat(participantToMovilizerQueue.getDeviceAddress(), is(moveletAssignmentDelete.getDeviceAddress()));
        assertThat(participantToMovilizerQueue.getMoveletKey(), is(moveletAssignmentDelete.getMoveletKey()));
        assertThat(participantToMovilizerQueue.getMoveletKeyExtension(), is(moveletAssignmentDelete.getMoveletKeyExtension()));
    }

    @Test
    public void testParticipantToMovilizerQueueParticipantConfigurationConstructor() throws Exception {
        participantToMovilizerQueue = new ParticipantToMovilizerQueue(participantConfiguration);

        assertThat(participantToMovilizerQueue.getParticipantConfiguration(), is(participantConfiguration));
        assertThat(participantToMovilizerQueue.getDeviceAddress(), is(participantConfiguration.getDeviceAddress()));
        assertThat(participantToMovilizerQueue.getName(), is(participantConfiguration.getName()));
        assertThat(participantToMovilizerQueue.getParticipantPasswordHashType(), is(participantConfiguration.getPasswordHashType()));
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
