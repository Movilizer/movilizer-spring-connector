package com.movilizer.mds.connector.model.consolidation;

import com.movilitas.movilizer.v15.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RequestConsolidationUtilCombinedTest extends RequestConsolidationBaseTest {

    @Test
    public void consolidate2MoveletsSameVersion() {
        String key = "movelet";
        Long version = 0L;

        MovilizerRequest request1 = createRequestWithMovelet("first", key, "", version);
        MovilizerRequest request2 = createRequestWithMovelet("second", key, "", version);
        MovilizerRequest request3 = createRequestWithMoveletAttribute("key", "ext",  "1");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2, request3);

        MovilizerRequest consolidatedRequest = consolidationUtil.consolidateRequests(requests);

        assertThat(consolidatedRequest.getMoveletSet().size(), is(2));
        assertThat(consolidatedRequest.getMoveletSet().get(0).getMovelet().size(), is(1));
        assertThat(consolidatedRequest.getMoveletSet().get(1).getMovelet().size(), is(1));

        MovilizerMovelet firstMovelet = consolidatedRequest.getMoveletSet().get(0).getMovelet().get(0);

        assertThat(firstMovelet.getName(), is("first"));
        assertThat(firstMovelet.getMoveletKey(), is(key));
        assertThat(firstMovelet.getMoveletKeyExtension(), is(""));
        assertThat(firstMovelet.getMoveletVersion(), is(version));

        MovilizerMovelet secondMovelet = consolidatedRequest.getMoveletSet().get(1).getMovelet().get(0);

        assertThat(secondMovelet.getName(), is("second"));
        assertThat(secondMovelet.getMoveletKey(), is(key));
        assertThat(secondMovelet.getMoveletKeyExtension(), is(""));
        assertThat(secondMovelet.getMoveletVersion(), is(version));

        assertThat(consolidatedRequest.getMoveletAttributeUpdate().size(), is(1));
        assertThat(consolidatedRequest.getMoveletAttributeUpdate().get(0).getAttributeUpdate().size(), is(1));

        MovilizerMoveletAttributeUpdate firstAttrUpdate = consolidatedRequest.getMoveletAttributeUpdate().get(0);

        assertThat(firstAttrUpdate.getMoveletKey(), is("key"));
        assertThat(firstAttrUpdate.getMoveletKeyExtension(), is("ext"));

        MovilizerAttributeEntry firstAttributeEntry = firstAttrUpdate.getAttributeUpdate().get(0);

        assertThat(firstAttributeEntry.getName(), is("entry"));
        assertThat(firstAttributeEntry.getValue(), is("1"));

    }

    private MovilizerRequest createRequestWithMovelet(String name, String key, String extKey,
                                                      Long version) {
        MovilizerMovelet movelet = new MovilizerMovelet();
        movelet.setMoveletKey(key);
        movelet.setMoveletKeyExtension(extKey);
        movelet.setName(name);
        movelet.setMoveletVersion(version);

        MovilizerMoveletSet set = new MovilizerMoveletSet();
        set.getMovelet().add(movelet);

        MovilizerRequest request = new MovilizerRequest();
        request.getMoveletSet().add(set);

        return request;
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
