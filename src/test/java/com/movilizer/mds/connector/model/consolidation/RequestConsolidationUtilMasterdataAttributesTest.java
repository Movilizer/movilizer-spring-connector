package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RequestConsolidationUtilMasterdataAttributesTest extends RequestConsolidationBaseTest {

    @Test
    public void consolidate2MasterdataAttributes() {

        MovilizerRequest request1 = createRequestWithMasterdataAttribute("key", "pool",  "1");
        MovilizerRequest request2 = createRequestWithMasterdataAttribute("key", "pool",  "2");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = RequestConsolidationUtil.consolidateRequests(requests, metrics);

        assertThat(consolidatedRequest.getMasterdataAttributeUpdate().size(), is(2));
        assertThat(consolidatedRequest.getMasterdataAttributeUpdate().get(0).getAttributeUpdate().size(), is(1));
        assertThat(consolidatedRequest.getMasterdataAttributeUpdate().get(1).getAttributeUpdate().size(), is(1));

        MovilizerMasterdataAttributeUpdate firstAttrUpdate = consolidatedRequest.getMasterdataAttributeUpdate().get(0);

        assertThat(firstAttrUpdate.getKey(), is("key"));
        assertThat(firstAttrUpdate.getPool(), is("pool"));

        MovilizerAttributeEntry firstAttributeEntry = firstAttrUpdate.getAttributeUpdate().get(0);

        assertThat(firstAttributeEntry.getName(), is("entry"));
        assertThat(firstAttributeEntry.getValue(), is("1"));

        MovilizerMasterdataAttributeUpdate secondAttributeUpdate = consolidatedRequest.getMasterdataAttributeUpdate().get(1);

        assertThat(secondAttributeUpdate.getKey(), is("key"));
        assertThat(secondAttributeUpdate.getPool(), is("pool"));

        MovilizerAttributeEntry secondParticipant = secondAttributeUpdate.getAttributeUpdate().get(0);

        assertThat(secondParticipant.getName(), is("entry"));
        assertThat(secondParticipant.getValue(), is("2"));
    }

    private MovilizerRequest createRequestWithMasterdataAttribute(String key, String pool, String value) {
        MovilizerAttributeEntry attributeEntry = new MovilizerAttributeEntry();
        attributeEntry.setName("entry");
        attributeEntry.setValue(value);

        MovilizerMasterdataAttributeUpdate attributeUpdate = new MovilizerMasterdataAttributeUpdate();
        attributeUpdate.getAttributeUpdate().add(attributeEntry);
        attributeUpdate.setKey(key);
        attributeUpdate.setPool(pool);

        MovilizerRequest request = new MovilizerRequest();
        request.getMasterdataAttributeUpdate().add(attributeUpdate);

        return request;
    }

}
