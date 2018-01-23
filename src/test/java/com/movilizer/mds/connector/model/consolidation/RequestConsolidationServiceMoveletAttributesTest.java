package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.MovilizerAttributeEntry;
import com.movilitas.movilizer.v15.MovilizerMoveletAttributeUpdate;
import com.movilitas.movilizer.v15.MovilizerRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RequestConsolidationServiceMoveletAttributesTest extends RequestConsolidationBaseTest {

    @Test
    public void consolidate2MoveletAttributes() {

        MovilizerRequest request1 = createRequestWithMoveletAttribute("key", "ext",  "1");
        MovilizerRequest request2 = createRequestWithMoveletAttribute("key", "ext",  "2");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = consolidationUtil.consolidateRequests(requests);

        assertThat(consolidatedRequest.getMoveletAttributeUpdate().size(), is(2));
        assertThat(consolidatedRequest.getMoveletAttributeUpdate().get(0).getAttributeUpdate().size(), is(1));
        assertThat(consolidatedRequest.getMoveletAttributeUpdate().get(1).getAttributeUpdate().size(), is(1));

        MovilizerMoveletAttributeUpdate firstAttrUpdate = consolidatedRequest.getMoveletAttributeUpdate().get(0);

        assertThat(firstAttrUpdate.getMoveletKey(), is("key"));
        assertThat(firstAttrUpdate.getMoveletKeyExtension(), is("ext"));

        MovilizerAttributeEntry firstAttributeEntry = firstAttrUpdate.getAttributeUpdate().get(0);

        assertThat(firstAttributeEntry.getName(), is("entry"));
        assertThat(firstAttributeEntry.getValue(), is("1"));

        MovilizerMoveletAttributeUpdate secondAttributeUpdate = consolidatedRequest.getMoveletAttributeUpdate().get(1);

        assertThat(secondAttributeUpdate.getMoveletKey(), is("key"));
        assertThat(secondAttributeUpdate.getMoveletKeyExtension(), is("ext"));

        MovilizerAttributeEntry secondParticipant = secondAttributeUpdate.getAttributeUpdate().get(0);

        assertThat(secondParticipant.getName(), is("entry"));
        assertThat(secondParticipant.getValue(), is("2"));
    }

    private MovilizerRequest createRequestWithMoveletAttribute(String key, String extension, String value) {
        MovilizerAttributeEntry attributeEntry = new MovilizerAttributeEntry();
        attributeEntry.setName("entry");
        attributeEntry.setValue(value);

        MovilizerMoveletAttributeUpdate attributeUpdate = new MovilizerMoveletAttributeUpdate();
        attributeUpdate.getAttributeUpdate().add(attributeEntry);
        attributeUpdate.setMoveletKey(key);
        attributeUpdate.setMoveletKeyExtension(extension);

        MovilizerRequest request = new MovilizerRequest();
        request.getMoveletAttributeUpdate().add(attributeUpdate);

        return request;
    }

}
