package com.movlizer.connector.v12.mapper;

import com.movilitas.movilizer.v12.MovilizerMasterdataDelete;
import com.movilitas.movilizer.v12.MovilizerMasterdataPoolUpdate;
import com.movilizer.connector.v12.service.mapper.MasterdataMapper;
import com.movilizer.connector.v12.service.mapper.MasterdataMapperCached;
import com.movlizer.connector.v12.mapper.models.DynamicPoolModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class MasterdataMapperSimpleTest {

    DynamicPoolModel model1;

    String user = "user1";

    String env = "test";

    String poolNameExpected = String.format("my_customers_%s_%s", user, env);

    String groupValue = DynamicPoolModel.group;

    String keyValue = "key value";

    String descValue = "descripion value";

    String entry1Value = "entry 1 value";

    DynamicPoolModel model2;

    String keyValue2 = "key value 2";

    String descValue2 = "descripion value 2";

    String entry1Value2 = "entry 1 value 2";

    @Before
    public void before() throws Exception {
        model1 = DynamicPoolModel.createTestObject(keyValue);

        model2 = new DynamicPoolModel(keyValue2);
        model2.setDesc(descValue2);
        model2.setEntry1name(entry1Value2);
    }

    @After
    public void after() {
    }

    @Test
    public void correctMapperSimpleTest() {
        MasterdataMapper mapper = new MasterdataMapper();
        List<DynamicPoolModel> instances = new ArrayList<>();
        instances.add(model1);
        instances.add(model2);

        MasterdataMapperCached masterdataMapperCached = mapper.getOrCreateMasterdataMapperCached(instances);
        assertThat(masterdataMapperCached, is(notNullValue()));
    }

    @Test
    public void deleteMasterdataSimpleTest() {
        MasterdataMapper mapper = new MasterdataMapper();
        List<DynamicPoolModel> instances = new ArrayList<>();
        instances.add(model1);
        instances.add(model2);

        MovilizerMasterdataPoolUpdate masterdataPoolUpdate = mapper.getMasterdata(instances,
                MasterdataMapper.Action.DELETE, user, env).iterator().next();
        assertThat(masterdataPoolUpdate.getPool(), is(poolNameExpected));
        assertThat(masterdataPoolUpdate.getDelete().size(), is(2));

        MovilizerMasterdataDelete deleteModel1 = null;
        MovilizerMasterdataDelete deleteModel2 = null;

        for (MovilizerMasterdataDelete masterdataDelete : masterdataPoolUpdate.getDelete()) {
            if (keyValue.equals(masterdataDelete.getKey())) {
                deleteModel1 = masterdataDelete;
            }
            if (keyValue2.equals(masterdataDelete.getKey())) {
                deleteModel2 = masterdataDelete;
            }
        }
        assertThat(deleteModel1, is(notNullValue()));
        assertThat(deleteModel1.getGroup(), is(groupValue));
        assertThat(deleteModel2, is(notNullValue()));
        assertThat(deleteModel2.getGroup(), is(groupValue));
    }
}
