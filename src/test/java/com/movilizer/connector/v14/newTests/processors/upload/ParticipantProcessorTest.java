package com.movilizer.connector.v14.newTests.processors.upload;

import com.movilitas.movilizer.v14.*;
import com.movilizer.connector.java.jobs.processors.upload.ParticipantProcessor;
import com.movilizer.connector.java.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.java.persistence.entities.ParticipantToMovilizerQueue;
import com.movilizer.connector.java.queues.ToMovilizerQueueService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class ParticipantProcessorTest {
    //Test data
    private final Long systemId = 1234L;
    private final String password = "pass";
    private ParticipantProcessor participantProcessor;
    private ToMovilizerQueueService toMovilizerQueueService;
    private MovilizerRequest request;

    private MovilizerParticipantConfiguration participant1;
    private MovilizerParticipantConfiguration participant2;
    private MovilizerMoveletAssignment participant3;
    private MovilizerMoveletAssignmentDelete participant4;
    private MovilizerParticipantReset participant5;

    private ParticipantToMovilizerQueue participantToMovilizerQueue1;
    private ParticipantToMovilizerQueue participantToMovilizerQueue2;
    private ParticipantToMovilizerQueue participantToMovilizerQueue3;
    private ParticipantToMovilizerQueue participantToMovilizerQueue4;
    private ParticipantToMovilizerQueue participantToMovilizerQueue5;

    @Before
    public void before() throws Exception {
        participantProcessor = new ParticipantProcessor();
        toMovilizerQueueService = mock(ToMovilizerQueueService.class);

        //Wire dependencies
        participantProcessor.setToMovilizerQueueService(toMovilizerQueueService);

        //prepare clean data
        request = new MovilizerRequest();
        request.setSystemId(systemId);
        request.setSystemPassword(password);

        //
        participant1 = new MovilizerParticipantConfiguration();
        participant1.setName("participant1");
        participant1.setDeviceAddress("participant1@participant1.com");
        participant2 = new MovilizerParticipantConfiguration();
        participant2.setName("participant2");
        participant2.setDeviceAddress("participant2@participant2.com");

        participant3 = new MovilizerMoveletAssignment();
        participant3.setMoveletKey("participant3");

        participant4 = new MovilizerMoveletAssignmentDelete();
        participant4.setMoveletKey("participant4");
        participant4.setDeviceAddress("participant4@participant4.com");

        participant5 = new MovilizerParticipantReset();
        participant5.setDeviceAddress("participant5@participant5.com");

        //
        participantToMovilizerQueue1 = new ParticipantToMovilizerQueue();
        participantToMovilizerQueue1.setName("participant1");
        participantToMovilizerQueue1.setDeviceAddress("participant1@participant1.com");
        participantToMovilizerQueue1.setParticipantConfiguration(participant1);
        participantToMovilizerQueue1.setAction(ParticipantToMovilizerQueue.Action.CONFIGURATION);

        participantToMovilizerQueue2 = new ParticipantToMovilizerQueue();
        participantToMovilizerQueue2.setName("participant2");
        participantToMovilizerQueue2.setDeviceAddress("participant2@participant2.com");
        participantToMovilizerQueue2.setParticipantConfiguration(participant2);
        participantToMovilizerQueue2.setAction(ParticipantToMovilizerQueue.Action.CONFIGURATION);

        participantToMovilizerQueue3 = new ParticipantToMovilizerQueue();
        participantToMovilizerQueue3.setMoveletKey("participant3");
        participantToMovilizerQueue3.setDeviceAddress("participant3@participant3.com");
        participantToMovilizerQueue3.setAssignment(participant3);
        participantToMovilizerQueue3.setAction(ParticipantToMovilizerQueue.Action.ASSIGN);

        participantToMovilizerQueue4 = new ParticipantToMovilizerQueue();
        participantToMovilizerQueue4.setMoveletKey("participant4");
        participantToMovilizerQueue4.setDeviceAddress("participant4");
        participantToMovilizerQueue4.setAssignmentDelete(participant4);
        participantToMovilizerQueue4.setAction(ParticipantToMovilizerQueue.Action.ASSIGN_DELETE);

        participantToMovilizerQueue5 = new ParticipantToMovilizerQueue();
        participantToMovilizerQueue5.setDeviceAddress("participant5@participant5.com");
        participantToMovilizerQueue5.setParticipantReset(participant5);
        participantToMovilizerQueue5.setAction(ParticipantToMovilizerQueue.Action.RESET);
    }

    @After
    public void after() {
    }

    @Test
    public void testProcessNoParticipantsInRequest() throws Exception {
        when(toMovilizerQueueService.getAllMasterdataUpdatesOrdered()).thenReturn(new ArrayList<>());
        //pre-condition
        assertThat(request.getMasterdataPoolUpdate().isEmpty(), is(true));

        participantProcessor.process(request);

        verify(toMovilizerQueueService, never()).offer(any((new MasterdataToMovilizerQueue()).getClass()));
    }

    @Test
    public void testProcessOneparticipantConfigurationInRequest() throws Exception {
        List<ParticipantToMovilizerQueue> participantToMovilizerQueues = new ArrayList<>();
        participantToMovilizerQueues.add(participantToMovilizerQueue1);

        when(toMovilizerQueueService.getAllParticipantUpdatesOrdered()).thenReturn(participantToMovilizerQueues);

        participantProcessor.process(request);

        assertThat(request.getParticipantConfiguration().size(), is(1));
        assertThat(request.getParticipantConfiguration().get(0).getDeviceAddress(), is("participant1@participant1.com"));
        assertThat(request.getParticipantConfiguration().get(0).getName(), is("participant1"));


//        can't test queue because mock
//        verify(toMovilizerQueueService, times(1)).offer(any((new MasterdataToMovilizerQueue()).getClass()));
//        assertThat(toMovilizerQueueService.getAllMasterdataUpdatesOrdered().size(), is(0));

    }

    @Test
    public void testProcessFiveParticipantsDifferentTypesInRequest() throws Exception {

        List<ParticipantToMovilizerQueue> participantToMovilizerQueues = new ArrayList<>();
        participantToMovilizerQueues.add(participantToMovilizerQueue1);
        participantToMovilizerQueues.add(participantToMovilizerQueue2);
        participantToMovilizerQueues.add(participantToMovilizerQueue3);
        participantToMovilizerQueues.add(participantToMovilizerQueue4);
        participantToMovilizerQueues.add(participantToMovilizerQueue5);

        when(toMovilizerQueueService.getAllParticipantUpdatesOrdered()).thenReturn(participantToMovilizerQueues);

        //add three participants to request

        participantProcessor.process(request);

        //TODO ERROR HERE -> size = 3 ¿?
        assertThat(request.getParticipantConfiguration().size(), is(2));
        assertThat(request.getParticipantConfiguration().get(0).getDeviceAddress(), is("participant1@participant1.com"));
        assertThat(request.getParticipantConfiguration().get(0).getName(), is("participant1"));
        assertThat(request.getParticipantConfiguration().get(1).getDeviceAddress(), is("participant2@participant2.com"));
        assertThat(request.getParticipantConfiguration().get(1).getName(), is("participant2"));

        assertThat(request.getMoveletAssignment().size(), is(1));
        assertThat(request.getMoveletAssignment().get(0).getMoveletKey(), is("participant3"));

        assertThat(request.getMoveletAssignmentDelete().size(), is(1));
        assertThat(request.getMoveletAssignmentDelete().get(0).getMoveletKey(), is("participant4"));
        assertThat(request.getMoveletAssignmentDelete().get(0).getDeviceAddress(), is("participant4@participant4.com"));

        assertThat(request.getParticipantReset().size(), is(1));
        assertThat(request.getParticipantReset().get(0).getDeviceAddress(), is("participant5@participant5.com"));


//        can't test queue because mock
//        verify(toMovilizerQueueService, times(1)).offer(any((new MasterdataToMovilizerQueue()).getClass()));
//        assertThat(toMovilizerQueueService.getAllMasterdataUpdatesOrdered().size(), is(0));

    }

}
