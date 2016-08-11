package com.movilizer.connector.newTests.processors.upload;

import com.movilitas.movilizer.v14.MovilizerMovelet;
import com.movilitas.movilizer.v14.MovilizerRequest;
import com.movilizer.connector.jobs.processors.upload.MoveletProcessor;
import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;
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


public class MoveletProcessorTest {
    //Test data
    private final Long systemId = 1234L;
    private final String password = "pass";
    private MoveletProcessor moveletProcessor;
    private ToMovilizerQueueService toMovilizerQueueService;
    private MovilizerRequest request;

    private MovilizerMovelet movelet1;
    private MovilizerMovelet movelet2;
    private MovilizerMovelet movelet3;

    private MoveletToMovilizerQueue moveletToMovilizerQueue1;
    private MoveletToMovilizerQueue moveletToMovilizerQueue2;
    private MoveletToMovilizerQueue moveletToMovilizerQueue3;

    @Before
    public void before() throws Exception {
        moveletProcessor = new MoveletProcessor();
        toMovilizerQueueService = mock(ToMovilizerQueueService.class);

        //Wire dependencies
        moveletProcessor.setToMovilizerQueueService(toMovilizerQueueService);

        //prepare clean data
        request = new MovilizerRequest();
        request.setSystemId(systemId);
        request.setSystemPassword(password);

        //
        movelet1 = new MovilizerMovelet();
        movelet1.setMoveletKey("movelet1");
        movelet2 = new MovilizerMovelet();
        movelet2.setMoveletKey("movelet2");
        movelet3 = new MovilizerMovelet();
        movelet3.setMoveletKey("movelet3");

        moveletToMovilizerQueue1 = new MoveletToMovilizerQueue();
        moveletToMovilizerQueue1.setMoveletKey("movelet1");
        moveletToMovilizerQueue1.setMovelet(movelet1);
        moveletToMovilizerQueue1.setAction(MoveletToMovilizerQueue.Action.UPDATE);

        moveletToMovilizerQueue2 = new MoveletToMovilizerQueue();
        moveletToMovilizerQueue2.setMoveletKey("movelet2");
        moveletToMovilizerQueue2.setAction(MoveletToMovilizerQueue.Action.UPDATE);
        moveletToMovilizerQueue2.setMovelet(movelet2);

        moveletToMovilizerQueue3 = new MoveletToMovilizerQueue();
        moveletToMovilizerQueue3.setMoveletKey("movelet2");
        moveletToMovilizerQueue3.setAction(MoveletToMovilizerQueue.Action.UPDATE);
        moveletToMovilizerQueue3.setMovelet(movelet3);
    }

    @After
    public void after() {
    }

    @Test
    public void testProcessNoMoveletsInRequest() throws Exception {
        when(toMovilizerQueueService.getAllMasterdataUpdatesOrdered()).thenReturn(new ArrayList<>());
        //pre-condition
        assertThat(request.getMasterdataPoolUpdate().isEmpty(), is(true));

        moveletProcessor.process(request);

        verify(toMovilizerQueueService, never()).offer(any((new MasterdataToMovilizerQueue()).getClass()));
    }

    @Test
    public void testProcessOneMoveletInRequest() throws Exception {
        List<MoveletToMovilizerQueue> moveletToMovilizerQueues = new ArrayList<>();
        moveletToMovilizerQueues.add(moveletToMovilizerQueue1);

        when(toMovilizerQueueService.getAllMoveletUpdatesOrdered()).thenReturn(moveletToMovilizerQueues);

        moveletProcessor.process(request);

        assertThat(request.getMoveletSet().size(), is(1));
        assertThat(request.getMoveletSet().get(0).getMovelet().size(), is(1));
        assertThat(request.getMoveletSet().get(0).getMovelet().get(0).getMoveletKey(), is("movelet1"));

//        can't test queue because mock
//        verify(toMovilizerQueueService, times(1)).offer(any((new MasterdataToMovilizerQueue()).getClass()));
//        assertThat(toMovilizerQueueService.getAllMasterdataUpdatesOrdered().size(), is(0));
    }

    @Test
    public void testProcessThreeMoveletsInRequest() throws Exception {

        List<MoveletToMovilizerQueue> moveletToMovilizerQueues = new ArrayList<>();

        moveletToMovilizerQueues.add(moveletToMovilizerQueue1);
        moveletToMovilizerQueues.add(moveletToMovilizerQueue2);
        moveletToMovilizerQueues.add(moveletToMovilizerQueue3);

        when(toMovilizerQueueService.getAllMoveletUpdatesOrdered()).thenReturn(moveletToMovilizerQueues);

        moveletProcessor.process(request);

        //pre-condition
        assertThat(request.getMoveletSet().size(), is(1));
        assertThat(request.getMoveletSet().get(0).getMovelet().size(), is(3));
        assertThat(request.getMoveletSet().get(0).getMovelet().get(0).getMoveletKey(), is("movelet1"));
        assertThat(request.getMoveletSet().get(0).getMovelet().get(1).getMoveletKey(), is("movelet2"));
        assertThat(request.getMoveletSet().get(0).getMovelet().get(2).getMoveletKey(), is("movelet3"));

//        can't test queue because mock
//        verify(toMovilizerQueueService, times(1)).offer(any((new MasterdataToMovilizerQueue()).getClass()));
//        assertThat(toMovilizerQueueService.getAllMasterdataUpdatesOrdered().size(), is(0));

    }

}
