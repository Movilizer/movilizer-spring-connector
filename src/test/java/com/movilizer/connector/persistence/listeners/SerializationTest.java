package com.movilizer.connector.persistence.listeners;

import com.movilitas.movilizer.v15.MovilizerGenericDataContainer;
import com.movilitas.movilizer.v15.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v15.MovilizerGenericUploadDataContainer;
import com.movilitas.movilizer.v15.MovilizerUploadDataContainer;
import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.persistence.entities.listeners.DatacontainerFromMovilizerQueueSerializerListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for the FST serialization engine
 *
 * @author Jesús de Mula Cano <jesus.demula@movilizer.com>
 * @version 0.1-SNAPSHOT, 2017.07.27
 * @since 0.1
 */

public class SerializationTest {
    private DatacontainerFromMovilizerQueueSerializerListener compressorListener;

    @Before
    public void before() throws Exception {
        compressorListener = new DatacontainerFromMovilizerQueueSerializerListener();
    }

    @After
    public void after() {
    }

    @Test
    public void testSimpleSerialization() throws Exception {
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName("testName");
        entry.setValstr("testValue");
        MovilizerGenericDataContainer genericDataContainer = new MovilizerGenericDataContainer();
        genericDataContainer.getEntry().add(entry);
        MovilizerGenericUploadDataContainer genericUploadDataContainer = new MovilizerGenericUploadDataContainer();
        genericUploadDataContainer.setData(genericDataContainer);
        MovilizerUploadDataContainer datacontainer = new MovilizerUploadDataContainer();
        datacontainer.setContainer(genericUploadDataContainer);
        DatacontainerFromMovilizerQueue queueEntry = new DatacontainerFromMovilizerQueue();
        queueEntry.setDatacontainer(datacontainer);

        assertThat(queueEntry.getSerializedDatacontainer(), is(nullValue()));

        compressorListener.setSerializedFieldsBeforeInsert(queueEntry);
        queueEntry.setDatacontainer(null);

        assertThat(queueEntry.getSerializedDatacontainer(), is(not(nullValue())));
        assertThat(queueEntry.getDatacontainer(), is(nullValue()));

        compressorListener.setDeserializedFieldsAfterSelect(queueEntry);

        assertThat(queueEntry.getDatacontainer(), is(not(nullValue())));
        assertThat(queueEntry.getDatacontainer().getContainer().getData().getEntry().size(), not(is(0)));
        assertThat(queueEntry.getDatacontainer().getContainer().getData().getEntry().get(0).getName(), is("testName"));
        assertThat(queueEntry.getDatacontainer().getContainer().getData().getEntry().get(0).getValstr(), is("testValue"));
    }
}
