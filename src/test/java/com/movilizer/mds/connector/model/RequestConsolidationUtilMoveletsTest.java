/*
 *
 * Copyright (c) 2012-2015 Movilizer GmbH,
 * Julius-Hatry-Straße 1, D-68163 Mannheim GmbH, Germany.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Movilizer GmbH ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Movilizer.
 */

package com.movilizer.mds.connector.model;

import com.movilitas.movilizer.v15.MovilizerMovelet;
import com.movilitas.movilizer.v15.MovilizerMoveletSet;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilizer.mds.connector.model.consolidation.RequestConsolidationUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RequestConsolidationUtilMoveletsTest extends RequestConsolidationBaseTest {

    @Test
    public void consolidate2MoveletsSameVersion() {
        String key = "movelet";
        Long version = 0L;

        MovilizerRequest request1 = createRequestWithMovelet("first", key, "", version);
        MovilizerRequest request2 = createRequestWithMovelet("second", key, "", version);

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = RequestConsolidationUtil.consolidateRequests(requests, metrics);

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

}
