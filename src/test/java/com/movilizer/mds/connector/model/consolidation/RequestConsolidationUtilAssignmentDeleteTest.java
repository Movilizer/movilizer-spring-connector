package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerMoveletAssignmentDelete;
import com.movilitas.movilizer.v15.MovilizerRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RequestConsolidationUtilAssignmentDeleteTest extends RequestConsolidationBaseTest {

    @Test
    public void consolidate2AssignmentDeletes() {

        MovilizerRequest request1 = createRequestWithAssignmentDelete("movKey", "ext", "+1");
        MovilizerRequest request2 = createRequestWithAssignmentDelete("movKey", "ext", "+2");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = consolidationUtil.consolidateRequests(requests);

        assertThat(consolidatedRequest.getMoveletAssignmentDelete().size(), is(2));

        MovilizerMoveletAssignmentDelete firstAssignmentDelete = consolidatedRequest.getMoveletAssignmentDelete().get(0);

        assertThat(firstAssignmentDelete.getMoveletKey(), is("movKey"));
        assertThat(firstAssignmentDelete.getMoveletKeyExtension(), is("ext"));
        assertThat(firstAssignmentDelete.getDeviceAddress(), is("+1"));

        MovilizerMoveletAssignmentDelete secondAssignmentDelete = consolidatedRequest.getMoveletAssignmentDelete().get(1);

        assertThat(secondAssignmentDelete.getMoveletKey(), is("movKey"));
        assertThat(secondAssignmentDelete.getMoveletKeyExtension(), is("ext"));
        assertThat(secondAssignmentDelete.getDeviceAddress(), is("+2"));
    }

    private MovilizerRequest createRequestWithAssignmentDelete(String moveletKey, String moveletExtKey,
                                                               String participantAddress) {
        MovilizerMoveletAssignmentDelete assignmentDelete = new MovilizerMoveletAssignmentDelete();
        assignmentDelete.setMoveletKey(moveletKey);
        assignmentDelete.setMoveletKeyExtension(moveletExtKey);
        assignmentDelete.setIgnoreExtensionKey(false);
        assignmentDelete.setDeviceAddress(participantAddress);
        assignmentDelete.setHardDelete(true);

        MovilizerRequest request = new MovilizerRequest();
        request.getMoveletAssignmentDelete().add(assignmentDelete);

        return request;
    }

}
