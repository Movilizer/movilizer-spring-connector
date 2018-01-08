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

import com.movilitas.movilizer.v15.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.movilizer.mds.connector.model.RequestConsolidationUtil.consolidateRequests;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class RequestConsolidationUtilMasterdataTest {

    @Test
    public void consolidate2MasterdataUpdatesDifferentPoolSameKey() {
        String key = "key";

        MovilizerRequest request1 = createRequestWithUpdate("a", "", key, "a");
        MovilizerRequest request2 = createRequestWithUpdate("b", null, key, "b");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMasterdataPoolUpdate().size(), is(2));
        MovilizerMasterdataPoolUpdate poolUpdateA;
        MovilizerMasterdataPoolUpdate poolUpdateB;
        if ("a".equals(consolidatedRequest.getMasterdataPoolUpdate().get(0).getPool())) {
            poolUpdateA = consolidatedRequest.getMasterdataPoolUpdate().get(0);
            poolUpdateB = consolidatedRequest.getMasterdataPoolUpdate().get(1);
        } else {
            poolUpdateA = consolidatedRequest.getMasterdataPoolUpdate().get(1);
            poolUpdateB = consolidatedRequest.getMasterdataPoolUpdate().get(0);
        }
        assertThat(poolUpdateA.getPool(), is("a"));
        assertThat(poolUpdateB.getPool(), is("b"));
        assertThat(poolUpdateA.getUpdate().size(), is(1));
        assertThat(poolUpdateB.getUpdate().size(), is(1));

        MovilizerMasterdataUpdate updateA = poolUpdateA.getUpdate().get(0);
        MovilizerMasterdataUpdate updateB = poolUpdateB.getUpdate().get(0);

        assertThat(updateA.getKey(), is(key));
        assertThat(updateB.getKey(), is(key));
    }

    @Test
    public void consolidate2MasterdataUpdatesSamePoolSameKey() {
        String key = "key";

        MovilizerRequest request1 = createRequestWithUpdate("a", "", key, "a");
        MovilizerRequest request2 = createRequestWithUpdate("a", null, key, "b");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMasterdataPoolUpdate().size(), is(1));
        MovilizerMasterdataPoolUpdate poolUpdateA;
        poolUpdateA = consolidatedRequest.getMasterdataPoolUpdate().get(0);


        assertThat(poolUpdateA.getPool(), is("a"));
        assertThat(poolUpdateA.getUpdate().size(), is(1));

        MovilizerMasterdataUpdate updateA = poolUpdateA.getUpdate().get(0);

        assertThat(updateA.getKey(), is(key));
        assertThat(updateA.getDescription(), is("b"));
    }

    @Test
    public void consolidate2MasterdataUpdatesDifferentGroupSameKey() {
        String pool = "pool";
        String key = "key";

        MovilizerRequest request1 = createRequestWithUpdate(pool, "a", key, "a");
        MovilizerRequest request2 = createRequestWithUpdate(pool, "b", key, "b");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMasterdataPoolUpdate().size(), is(1));
        assertThat(consolidatedRequest.getMasterdataPoolUpdate().get(0).getPool(), is(pool));
        assertThat(consolidatedRequest.getMasterdataPoolUpdate().get(0).getUpdate().size(), is(1));

        MovilizerMasterdataUpdate update = consolidatedRequest.getMasterdataPoolUpdate().get(0).getUpdate().get(0);

        assertThat(update.getGroup(), is("b"));
        assertThat(update.getKey(), is(key));
        assertThat(update.getDescription(), is("b"));
    }

    @Test
    public void consolidate2MasterdataUpdatesDifferentGroup1ReferenceThenDeletePool() {
        String pool = "pool";

        MovilizerRequest request1 = createRequestWithUpdate(pool, "a", "1", "a");
        MovilizerRequest request2 = createRequestWithUpdate(pool, "b", "2", "b");
        MovilizerRequest request3 = createRequestWithReference(pool, "a", "2");
        MovilizerRequest request4 = createRequestWithDelete(pool, null, null);

        List<MovilizerRequest> requests = Arrays.asList(request1, request2, request3, request4);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMasterdataPoolUpdate().size(), is(1));
        assertThat(consolidatedRequest.getMasterdataPoolUpdate().get(0).getPool(), is(pool));

        assertThat(consolidatedRequest.getMasterdataPoolUpdate().get(0).getDelete().size(), is(1));

        MovilizerMasterdataDelete delete = consolidatedRequest.getMasterdataPoolUpdate().get(0).getDelete().get(0);
        assertThat(delete.getGroup(), is(nullValue()));
        assertThat(delete.getKey(), is(nullValue()));
    }

    @Test
    public void consolidateMasterdataUpdateThenDeletePoolThenUpdate() {
        String pool = "pool";

        MovilizerRequest request1 = createRequestWithUpdate(pool, "a", "1", "a");
        MovilizerRequest request2 = createRequestWithDelete(pool, null, null);
        MovilizerRequest request3 = createRequestWithUpdate(pool, "a", "1", "b");

        List<MovilizerRequest> requests = Arrays.asList(request1, request2, request3);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMasterdataPoolUpdate().size(), is(2));

        MovilizerMasterdataDelete delete;
        MovilizerMasterdataPoolUpdate poolUpdateA;

        if (consolidatedRequest.getMasterdataPoolUpdate().get(0).getDelete().isEmpty()) {
            delete = consolidatedRequest.getMasterdataPoolUpdate().get(1).getDelete().get(0);
            poolUpdateA = consolidatedRequest.getMasterdataPoolUpdate().get(0);
        } else {
            delete = consolidatedRequest.getMasterdataPoolUpdate().get(0).getDelete().get(0);
            poolUpdateA = consolidatedRequest.getMasterdataPoolUpdate().get(1);
        }

        assertThat(delete.getGroup(), is(nullValue()));
        assertThat(delete.getKey(), is(nullValue()));

        assertThat(poolUpdateA.getPool(), is(pool));
        assertThat(poolUpdateA.getUpdate().size(), is(1));

        MovilizerMasterdataUpdate updateA = poolUpdateA.getUpdate().get(0);

        assertThat(updateA.getKey(), is("1"));
        assertThat(updateA.getDescription(), is("b"));
    }

    @Test
    public void consolidate2MasterdataUpdates1ReferenceThenDeleteGroup() {
        String pool = "pool";

        MovilizerRequest request1 = createRequestWithUpdate(pool, "a", "1", "a");
        MovilizerRequest request2 = createRequestWithUpdate(pool, "b", "2", "b");
        MovilizerRequest request3 = createRequestWithReference(pool, "a", "2");
        MovilizerRequest request4 = createRequestWithDelete(pool, "a", null);

        List<MovilizerRequest> requests = Arrays.asList(request1, request2, request3, request4);

        MovilizerRequest consolidatedRequest = consolidateRequests(requests);

        assertThat(consolidatedRequest.getMasterdataPoolUpdate().size(), is(2));

        MovilizerMasterdataDelete delete;
        MovilizerMasterdataPoolUpdate poolUpdateB;

        if (consolidatedRequest.getMasterdataPoolUpdate().get(0).getDelete().isEmpty()) {
            delete = consolidatedRequest.getMasterdataPoolUpdate().get(1).getDelete().get(0);
            poolUpdateB = consolidatedRequest.getMasterdataPoolUpdate().get(0);
        } else {
            delete = consolidatedRequest.getMasterdataPoolUpdate().get(0).getDelete().get(0);
            poolUpdateB = consolidatedRequest.getMasterdataPoolUpdate().get(1);
        }

        assertThat(delete.getGroup(), is("a"));
        assertThat(delete.getKey(), is(nullValue()));

        assertThat(poolUpdateB.getPool(), is(pool));
        assertThat(poolUpdateB.getUpdate().size(), is(1));

        MovilizerMasterdataUpdate updateA = poolUpdateB.getUpdate().get(0);

        assertThat(updateA.getKey(), is("2"));
        assertThat(updateA.getDescription(), is("b"));
    }

    private MovilizerRequest createRequestWithUpdate(String pool, String group, String key, String description) {
        MovilizerMasterdataUpdate update = new MovilizerMasterdataUpdate();
        update.setGroup(group);
        update.setKey(key);
        update.setDescription(description);

        MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
        poolUpdate.setPool(pool);
        poolUpdate.getUpdate().add(update);

        MovilizerRequest request = new MovilizerRequest();
        request.getMasterdataPoolUpdate().add(poolUpdate);

        return request;
    }

    private MovilizerRequest createRequestWithReference(String pool, String group, String key) {
        MovilizerMasterdataReference reference = new MovilizerMasterdataReference();
        reference.setGroup(group);
        reference.setKey(key);

        MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
        poolUpdate.setPool(pool);
        poolUpdate.getReference().add(reference);

        MovilizerRequest request = new MovilizerRequest();
        request.getMasterdataPoolUpdate().add(poolUpdate);

        return request;
    }

    private MovilizerRequest createRequestWithDelete(String pool, String group, String key) {
        MovilizerMasterdataDelete delete = new MovilizerMasterdataDelete();
        delete.setGroup(group);
        delete.setKey(key);

        MovilizerMasterdataPoolUpdate poolUpdate = new MovilizerMasterdataPoolUpdate();
        poolUpdate.setPool(pool);
        poolUpdate.getDelete().add(delete);

        MovilizerRequest request = new MovilizerRequest();
        request.getMasterdataPoolUpdate().add(poolUpdate);

        return request;
    }

}
