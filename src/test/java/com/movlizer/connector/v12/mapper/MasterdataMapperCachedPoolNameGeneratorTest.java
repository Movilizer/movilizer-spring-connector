package com.movlizer.connector.v12.mapper;

import com.movilizer.connector.v12.service.mapper.MasterdataMapperCached;
import com.movlizer.connector.v12.mapper.models.DynamicPoolModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class MasterdataMapperCachedPoolNameGeneratorTest {

//    DynamicPoolModel model;
//
//    String user = "user1";
//
//    String env = "test";
//
//    String poolNameExpected = String.format("my_customers_%s_%s", user, env);
//
//    String keyName = "key";
//
//    String groupName = "group";
//
//    String descName = "desc";
//
//    String entry1Name = "entry1name";
//
//    String keyValue = "key value";
//
//    String groupValue = DynamicPoolModel.group;
//
//    String descValue = "descripion value";
//
//    String entry1Value = "entry 1 value";
//
//    @Before
//    public void before() throws Exception {
//        model = new DynamicPoolModel(keyValue);
//        model.setDesc(descValue);
//        model.setEntry1name(entry1Value);
//    }
//
//    @After
//    public void after() {
//    }
//
//    @Test
//    public void getDynamicPoolNameTest() {
//        MasterdataMapperCached mapper = new MasterdataMapperCached(DynamicPoolModel.class);
//        assertThat(mapper, is(notNullValue()));
//        assertThat(mapper.getPoolNameMethod(), is(notNullValue()));
//
//        assertThat(mapper.getPool(model, user, env), is(poolNameExpected));
//    }
}
