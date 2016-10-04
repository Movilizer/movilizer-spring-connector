package com.movilizer.connector.newTests.processors.upload;

import com.movilitas.movilizer.v15.*;
import com.movilizer.connector.jobs.processors.upload.MasterdataProcessor;
import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.queues.ToMovilizerQueueService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class MasterdataProcessorTest {
    //Test data
    private final Long systemId = 1234L;
    private final String password = "pass";
    private MasterdataProcessor masterdataProcessor;
    private ToMovilizerQueueService toMovilizerQueueService;
    private MovilizerRequest request;

    private MovilizerMasterdataUpdate movilizerMasterdataUpdate1;
    private MovilizerMasterdataUpdate movilizerMasterdataUpdate2;
    private MovilizerMasterdataUpdate movilizerMasterdataUpdate3;

    private MasterdataToMovilizerQueue masterdataToMovilizerQueue1;
    private MasterdataToMovilizerQueue masterdataToMovilizerQueue2;
    private MasterdataToMovilizerQueue masterdataToMovilizerQueue3;

    @Before
    public void before() throws Exception {
        masterdataProcessor = new MasterdataProcessor();
        toMovilizerQueueService = mock(ToMovilizerQueueService.class);

        //Wire dependencies
        masterdataProcessor.setToMovilizerQueueService(toMovilizerQueueService);

        //prepare clean data
        request = new MovilizerRequest();
        request.setSystemId(systemId);
        request.setSystemPassword(password);

        //
        MovilizerGenericDataContainerEntry movilizerGenericDataContainerEntry = new MovilizerGenericDataContainerEntry();
        movilizerGenericDataContainerEntry.setName("exampleName");
        movilizerGenericDataContainerEntry.setValstr("exampleValue");
        MovilizerGenericDataContainer movilizerGenericDataContainer = new MovilizerGenericDataContainer();
        movilizerGenericDataContainer.getEntry().add(movilizerGenericDataContainerEntry);
        movilizerMasterdataUpdate1 = new MovilizerMasterdataUpdate();
        movilizerMasterdataUpdate1.setData(movilizerGenericDataContainer);
        movilizerMasterdataUpdate1.setKey("masterdata1");
        movilizerMasterdataUpdate2 = new MovilizerMasterdataUpdate();
        movilizerMasterdataUpdate2.setData(movilizerGenericDataContainer);
        movilizerMasterdataUpdate2.setKey("masterdata2");
        movilizerMasterdataUpdate3 = new MovilizerMasterdataUpdate();
        movilizerMasterdataUpdate3.setData(movilizerGenericDataContainer);
        movilizerMasterdataUpdate3.setKey("masterdata3");

        //
        masterdataToMovilizerQueue1 = new MasterdataToMovilizerQueue();
        masterdataToMovilizerQueue1.setKey("masterdata1");
        masterdataToMovilizerQueue1.setPool("pool1");
        masterdataToMovilizerQueue1.setAction(MasterdataToMovilizerQueue.Action.UPDATE);
        masterdataToMovilizerQueue1.setMasterdataUpdate(movilizerMasterdataUpdate1);

        masterdataToMovilizerQueue2 = new MasterdataToMovilizerQueue();
        masterdataToMovilizerQueue2.setKey("masterdata2");
        masterdataToMovilizerQueue2.setPool("pool1");
        masterdataToMovilizerQueue2.setAction(MasterdataToMovilizerQueue.Action.UPDATE);
        masterdataToMovilizerQueue2.setMasterdataUpdate(movilizerMasterdataUpdate2);

        masterdataToMovilizerQueue3 = new MasterdataToMovilizerQueue();
        masterdataToMovilizerQueue3.setKey("masterdata3");
        masterdataToMovilizerQueue3.setPool("pool2");
        masterdataToMovilizerQueue3.setAction(MasterdataToMovilizerQueue.Action.UPDATE);
        masterdataToMovilizerQueue3.setMasterdataUpdate(movilizerMasterdataUpdate3);
    }

    @After
    public void after() {
    }

    @Test
    public void testProcessNoMasterdataInRequest() throws Exception {
        when(toMovilizerQueueService.getAllMasterdataUpdatesOrdered()).thenReturn(new ArrayList<>());
        //pre-condition
        assertThat(request.getMasterdataPoolUpdate().isEmpty(), is(true));

        masterdataProcessor.process(request);

        verify(toMovilizerQueueService, never()).offer(any((new MasterdataToMovilizerQueue()).getClass()));
    }

    @Test
    public void testProcessOneMasterdataInRequest() throws Exception {
        List<MasterdataToMovilizerQueue> masterdataToMovilizerQueues = new ArrayList<>();
        masterdataToMovilizerQueues.add(masterdataToMovilizerQueue1);

        when(toMovilizerQueueService.getAllMasterdataUpdatesOrdered()).thenReturn(masterdataToMovilizerQueues);

        //add one masterdata to request
        MovilizerMasterdataPoolUpdate movilizerMasterdataPoolUpdate = new MovilizerMasterdataPoolUpdate();
        movilizerMasterdataPoolUpdate.getUpdate().add(movilizerMasterdataUpdate1);
        movilizerMasterdataPoolUpdate.setPool("pool1");

        masterdataProcessor.process(request);


        assertThat(request.getMasterdataPoolUpdate().size(), is(1));
        assertThat(request.getMasterdataPoolUpdate().get(0).getPool(), is("pool1"));
        assertThat(request.getMasterdataPoolUpdate().get(0).getUpdate().size(), is(1));
        assertThat(request.getMasterdataPoolUpdate().get(0).getUpdate().get(0).getData().getEntry().get(0).getName(), is("exampleName"));
        assertThat(request.getMasterdataPoolUpdate().get(0).getUpdate().get(0).getData().getEntry().get(0).getValstr(), is("exampleValue"));

        verify(toMovilizerQueueService, times(1)).removeMasterdata(masterdataToMovilizerQueues);

    }

    @Test
    public void testProcessThreeMasterdataDifferentPoolsInRequest() throws Exception {
        List<MasterdataToMovilizerQueue> masterdataToMovilizerQueues = new ArrayList<>();
        masterdataToMovilizerQueues.add(masterdataToMovilizerQueue1);
        masterdataToMovilizerQueues.add(masterdataToMovilizerQueue2);
        masterdataToMovilizerQueues.add(masterdataToMovilizerQueue3);

        when(toMovilizerQueueService.getAllMasterdataUpdatesOrdered()).thenReturn(masterdataToMovilizerQueues);

        masterdataProcessor.process(request);

        assertThat(request.getMasterdataPoolUpdate().size(), is(2));

        MovilizerMasterdataPoolUpdate movilizerMasterdataPoolUpdate1;
        MovilizerMasterdataPoolUpdate movilizerMasterdataPoolUpdate2;
        if (request.getMasterdataPoolUpdate().get(0).getPool().equals("pool1")) {
            movilizerMasterdataPoolUpdate1 = request.getMasterdataPoolUpdate().get(0);
            movilizerMasterdataPoolUpdate2 = request.getMasterdataPoolUpdate().get(1);
        } else {
            movilizerMasterdataPoolUpdate1 = request.getMasterdataPoolUpdate().get(1);
            movilizerMasterdataPoolUpdate2 = request.getMasterdataPoolUpdate().get(0);
        }
        assertThat(movilizerMasterdataPoolUpdate1.getPool(), is("pool1"));
        assertThat(movilizerMasterdataPoolUpdate1.getUpdate().size(), is(2));

        assertThat(movilizerMasterdataPoolUpdate2.getPool(), is("pool2"));
        assertThat(movilizerMasterdataPoolUpdate2.getUpdate().size(), is(1));

        verify(toMovilizerQueueService, times(1)).removeMasterdata(masterdataToMovilizerQueues);
    }

}
