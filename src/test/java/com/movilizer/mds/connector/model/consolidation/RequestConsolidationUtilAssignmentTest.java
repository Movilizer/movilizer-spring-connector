package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RequestConsolidationUtilAssignmentTest extends RequestConsolidationBaseTest {

    @Test
    public void consolidate2Assignments() {

        MovilizerRequest request1 = createRequestWithAssignment("movKey", "ext",  "user a", "+1");
        MovilizerRequest request2 = createRequestWithAssignment("movKey", "ext",  "user b", "+2");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = RequestConsolidationUtil.consolidateRequests(requests, metrics);

        assertThat(consolidatedRequest.getMoveletAssignment().size(), is(2));

        MovilizerMoveletAssignment firstAssignment = consolidatedRequest.getMoveletAssignment().get(0);

        assertThat(firstAssignment.getMoveletKey(), is("movKey"));
        assertThat(firstAssignment.getMoveletKeyExtension(), is("ext"));
        assertThat(firstAssignment.getParticipant().size(), is(1));

        MovilizerParticipant firstParticipant = firstAssignment.getParticipant().get(0);

        assertThat(firstParticipant.getName(), is("user a"));
        assertThat(firstParticipant.getDeviceAddress(), is("+1"));

        MovilizerMoveletAssignment secondAssignment = consolidatedRequest.getMoveletAssignment().get(1);

        assertThat(secondAssignment.getMoveletKey(), is("movKey"));
        assertThat(secondAssignment.getMoveletKeyExtension(), is("ext"));
        assertThat(secondAssignment.getParticipant().size(), is(1));

        MovilizerParticipant secondParticipant = secondAssignment.getParticipant().get(0);

        assertThat(secondParticipant.getName(), is("user b"));
        assertThat(secondParticipant.getDeviceAddress(), is("+2"));
    }

    private MovilizerRequest createRequestWithAssignment(String moveletKey, String moveletExtKey, String participantName,
                                                         String participantAddress) {
        MovilizerMoveletAssignment assignment = new MovilizerMoveletAssignment();
        assignment.setMoveletKey(moveletKey);
        assignment.setMoveletKeyExtension(moveletExtKey);
        assignment.setIgnoreExtensionKey(false);

        MovilizerParticipant participant = new MovilizerParticipant();
        participant.setName(participantName);
        participant.setDeviceAddress(participantAddress);
        assignment.getParticipant().add(participant);

        MovilizerRequest request = new MovilizerRequest();
        request.getMoveletAssignment().add(assignment);

        return request;
    }

}
