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

package com.movilizer.mds.connector.mapper.masterdata;

import com.movilitas.movilizer.v15.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v15.MovilizerMasterdataPoolUpdate;
import com.movilitas.movilizer.v15.MovilizerMasterdataUpdate;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.mapper.datacontainer.GenericDataContainerMapperImpl;
import com.movilizer.mds.connector.mapper.masterdata.models.DynamicPoolModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class MasterDataObjectMapperTest {

    DynamicPoolModel model1;

    DynamicPoolModel model2;

    String keyValue = "key value";

    String keyValue2 = "key value 2";

    private MasterdataMapper mapper;
    @Mock
    private MovilizerMetricService metricService;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        mapper = new MasterdataMapper(metricService, new GenericDataContainerMapperImpl(metricService));
        model1 = DynamicPoolModel.createTestObject(keyValue);
        model2 = DynamicPoolModel.createTestObject(keyValue2);
    }

    @Test
    public void convertObjectToMD() {
        List<DynamicPoolModel> instances = new ArrayList<>();
        instances.add(model1);

        Collection<MovilizerMasterdataPoolUpdate> updates = mapper.getMasterdata(instances,
                MasterdataMapper.Action.UPDATE, "0", "0");

        assertThat(updates.isEmpty(), is(false));
        MovilizerMasterdataPoolUpdate poolupdate = updates.iterator().next();

        MovilizerMasterdataUpdate update = poolupdate.getUpdate().get(0);
        assertThat(update, is(notNullValue()));

        assertThat(update.getData().getEntry().size(), is(6));

        MovilizerGenericDataContainerEntry entryKey = getEntry(update, "key");
        assertThat(entryKey.getValstr(), is(keyValue));

        MovilizerGenericDataContainerEntry entryDesc = getEntry(update, "desc");
        assertThat(entryDesc.getValstr(), is("descValue"));

        MovilizerGenericDataContainerEntry entry1 = getEntry(update, "entry1name");
        assertThat(entry1.getValstr(), is("entryValue"));

        MovilizerGenericDataContainerEntry entryObject = getEntry(update, "testObject");
        assertThat(getEntry(entryObject.getEntry(), "intField").getValstr(), is("100"));

        MovilizerGenericDataContainerEntry entryList = getEntry(update, "list");
        assertThat(entryList.getEntry().size(), is(1));
        assertThat(entryList.getEntry().get(0).getName(), is("0"));

        MovilizerGenericDataContainerEntry entryMap = getEntry(update, "map");
        assertThat(entryMap.getEntry().size(), is(1));
        assertThat(entryMap.getEntry().get(0).getName(), is("testKey"));

    }

    public MovilizerGenericDataContainerEntry getEntry(MovilizerMasterdataUpdate update, String key) {
        return getEntry(update.getData().getEntry(), key);
    }

    public MovilizerGenericDataContainerEntry getEntry(
            List<MovilizerGenericDataContainerEntry> updates, String key) {
        for (MovilizerGenericDataContainerEntry entry : updates) {
            if (entry.getName().equals(key)) {
                return entry;
            }
        }
        return null;
    }
}
