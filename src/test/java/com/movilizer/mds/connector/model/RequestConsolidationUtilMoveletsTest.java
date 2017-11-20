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
import com.movilitas.movilizer.v15.MovilizerMoveletDelete;
import com.movilitas.movilizer.v15.MovilizerMoveletSet;
import com.movilitas.movilizer.v15.MovilizerRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.movilizer.mds.connector.model.RequestConsolidationUtil.consolidateRequests;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RequestConsolidationUtilMoveletsTest {

    @Test
    public void consolidate2MoveletsSameVersion() {
        String key = "movelet";
        Long version = 0L;

        MovilizerRequest request1 = createRequestWithMovelet("first", key, "", version);
        MovilizerRequest request2 = createRequestWithMovelet("second", key, "", version);

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMoveletSet().size(), is(1));
        assertThat(consolidatedRequest.getMoveletSet().get(0).getMovelet().size(), is(1));

        MovilizerMovelet consolidateMovelet =
                consolidatedRequest.getMoveletSet().get(0).getMovelet().get(0);

        assertThat(consolidateMovelet.getName(), is("first"));
        assertThat(consolidateMovelet.getMoveletKey(), is(key));
        assertThat(consolidateMovelet.getMoveletKeyExtension(), is(""));
        assertThat(consolidateMovelet.getMoveletVersion(), is(version));
    }

    @Test
    public void consolidateMoveletHighVersionThenMoveletLowVersion() {
        String key = "movelet";

        MovilizerRequest request1 = createRequestWithMovelet("first", key, "", 1L);
        MovilizerRequest request2 = createRequestWithMovelet("second", key, "", 0L);

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMoveletSet().size(), is(1));
        assertThat(consolidatedRequest.getMoveletSet().get(0).getMovelet().size(), is(1));

        MovilizerMovelet consolidateMovelet =
                consolidatedRequest.getMoveletSet().get(0).getMovelet().get(0);

        assertThat(consolidateMovelet.getName(), is("first"));
        assertThat(consolidateMovelet.getMoveletKey(), is(key));
        assertThat(consolidateMovelet.getMoveletKeyExtension(), is(""));
        assertThat(consolidateMovelet.getMoveletVersion(), is(1L));
    }

    @Test
    public void consolidateMoveletThenDelete() {
        String key = "movelet";

        MovilizerRequest requestWithMovelet = createRequestWithMovelet("mov", key, "", 1L);
        MovilizerRequest requestWithDelete = createRequestWithMoveletDelete( key, "", true);

        List<MovilizerRequest> requests = Arrays.asList(requestWithMovelet, requestWithDelete);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMoveletSet().size(), is(0));
        assertThat(consolidatedRequest.getMoveletDelete().size(), is(1));

        MovilizerMoveletDelete consolidateMoveletDelete =
                consolidatedRequest.getMoveletDelete().get(0);

        assertThat(consolidateMoveletDelete.getMoveletKey(), is(key));
        assertThat(consolidateMoveletDelete.getMoveletKeyExtension(), is(""));
    }

    @Test
    public void consolidateMoveletHighVersionThenDeleteThenMoveletLowVersion() {
        String key = "movelet";

        MovilizerRequest requestWithMoveletHigh = createRequestWithMovelet("high", key, "", 1L);
        MovilizerRequest requestWithDelete = createRequestWithMoveletDelete( key, "", true);
        MovilizerRequest requestWithMoveletLow = createRequestWithMovelet("low", key, "", 0L);

        List<MovilizerRequest> requests = Arrays.asList(requestWithMoveletHigh, requestWithDelete,
                requestWithMoveletLow);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMoveletSet().size(), is(1));
        assertThat(consolidatedRequest.getMoveletSet().get(0).getMovelet().size(), is(1));

        MovilizerMovelet consolidateMovelet =
                consolidatedRequest.getMoveletSet().get(0).getMovelet().get(0);

        assertThat(consolidateMovelet.getName(), is("low"));
        assertThat(consolidateMovelet.getMoveletKey(), is(key));
        assertThat(consolidateMovelet.getMoveletKeyExtension(), is(""));
        assertThat(consolidateMovelet.getMoveletVersion(), is(0L));

        assertThat(consolidatedRequest.getMoveletDelete().size(), is(1));

        MovilizerMoveletDelete consolidateMoveletDelete =
                consolidatedRequest.getMoveletDelete().get(0);

        assertThat(consolidateMoveletDelete.getMoveletKey(), is(key));
        assertThat(consolidateMoveletDelete.getMoveletKeyExtension(), is(""));
    }

//    @Test
//    public void consolidateMoveletHighVersionThenDeleteWithExtensionThenMoveletLowVersion() {
//        String key = "movelet";
//
//        MovilizerRequest requestWithMoveletHigh = createRequestWithMovelet("high", key, "ext", 1L);
//        MovilizerRequest requestWithDelete = createRequestWithMoveletDelete( key, "ext", false);
//        MovilizerRequest requestWithMoveletLow = createRequestWithMovelet("low", key, "", 0L);
//
//        List<MovilizerRequest> requests = Arrays.asList(requestWithMoveletHigh, requestWithDelete,
//                requestWithMoveletLow);
//
//        MovilizerRequest consolidatedRequest = consolidateRequests(requests);
//
//        assertThat(consolidatedRequest.getMoveletSet().size(), is(1));
//        assertThat(consolidatedRequest.getMoveletSet().get(0).getMovelet().size(), is(1));
//
//        MovilizerMovelet consolidateMovelet =
//                consolidatedRequest.getMoveletSet().get(0).getMovelet().get(0);
//
//        assertThat(consolidateMovelet.getName(), is("low"));
//        assertThat(consolidateMovelet.getMoveletKey(), is(key));
//        assertThat(consolidateMovelet.getMoveletKeyExtension(), is(""));
//        assertThat(consolidateMovelet.getMoveletVersion(), is(0L));
//
//        assertThat(consolidatedRequest.getMoveletDelete().size(), is(1));
//
//        MovilizerMoveletDelete consolidateMoveletDelete =
//                consolidatedRequest.getMoveletDelete().get(0);
//
//        assertThat(consolidateMoveletDelete.getMoveletKey(), is(key));
//        assertThat(consolidateMoveletDelete.getMoveletKeyExtension(), is(""));
//    }

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

    private MovilizerRequest createRequestWithMoveletDelete(String key, String extKey,
                                                            Boolean ignoreExtension) {
        MovilizerMoveletDelete delete = new MovilizerMoveletDelete();
        delete.setMoveletKey(key);
        delete.setMoveletKeyExtension(extKey);
        delete.setIgnoreExtensionKey(ignoreExtension);

        MovilizerRequest request = new MovilizerRequest();
        request.getMoveletDelete().add(delete);

        return request;
    }

}
