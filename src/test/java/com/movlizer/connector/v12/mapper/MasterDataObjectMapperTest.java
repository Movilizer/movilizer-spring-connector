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

package com.movlizer.connector.v12.mapper;

import com.movilitas.movilizer.v12.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v12.MovilizerMasterdataPoolUpdate;
import com.movilitas.movilizer.v12.MovilizerMasterdataUpdate;
import com.movilizer.connector.v12.service.mapper.MasterdataMapper;
import com.movlizer.connector.v12.mapper.models.DynamicPoolModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class MasterDataObjectMapperTest {

//    DynamicPoolModel model1;
//
//    DynamicPoolModel model2;
//
//    String keyValue = "key value";
//
//    String keyValue2 = "key value 2";
//
//    @Before
//    public void before() throws Exception {
//        model1 = DynamicPoolModel.createTestObject(keyValue);
//        model2 = DynamicPoolModel.createTestObject(keyValue2);
//    }
//
//    @Test
//    public void convertObjectToMD() {
//        MasterdataMapper mapper = new MasterdataMapper();
//        List<DynamicPoolModel> instances = new ArrayList<>();
//        instances.add(model1);
//
//        Collection<MovilizerMasterdataPoolUpdate> updates = mapper.getMasterdata(instances,
//                MasterdataMapper.Action.UPDATE, "0", "0");
//
//        updates.isEmpty();
//        assertThat(updates.isEmpty(), is(false));
//        MovilizerMasterdataPoolUpdate poolupdate = updates.iterator().next();
//
//        MovilizerMasterdataUpdate update = poolupdate.getUpdate().get(0);
//        assertThat(update, is(notNullValue()));
//
//        assertThat(update.getData().getEntry().size(), is(4));
//
//        update.getData().getEntry().get(0);
//
//        MovilizerGenericDataContainerEntry entry1 = getEntry(update, "entry1name");
//        assertThat(entry1.getValstr(), is("entryValue"));
//
//        MovilizerGenericDataContainerEntry entryObject = getEntry(update, "testObject");
//        assertThat(getEntry(entryObject.getEntry(), "intField").getValstr(), is("100"));
//
//        MovilizerGenericDataContainerEntry entryList = getEntry(update, "list");
//        assertThat(entryList.getEntry().size(), is(1));
//        assertThat(entryList.getEntry().get(0).getName(), is("0"));
//
//        MovilizerGenericDataContainerEntry entryMap = getEntry(update, "map");
//        assertThat(entryMap.getEntry().size(), is(1));
//        assertThat(entryMap.getEntry().get(0).getName(), is("testKey"));
//
//    }
//
//    public MovilizerGenericDataContainerEntry getEntry(MovilizerMasterdataUpdate update, String key) {
//        return getEntry(update.getData().getEntry(), key);
//    }
//
//    public MovilizerGenericDataContainerEntry getEntry(
//            List<MovilizerGenericDataContainerEntry> updates, String key) {
//        for (MovilizerGenericDataContainerEntry entry : updates) {
//            if (entry.getName().equals(key)) {
//                return entry;
//            }
//        }
//        return null;
//    }
}
