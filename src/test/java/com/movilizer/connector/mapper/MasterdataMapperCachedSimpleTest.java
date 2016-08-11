package com.movilizer.connector.mapper;

import static org.hamcrest.MatcherAssert.assertThat;

public class MasterdataMapperCachedSimpleTest {

//    SimpleCorrectModel simpleCorrectModel;
//
//    String pool = "simple_pool";
//
//    String keyName = "key";
//
//    String groupName = "group";
//
//    String descName = "desc";
//
//    String filter1Name = "filter1name";
//
//    String filter2Name = "filter2name";
//
//    String filter3Name = "filter3name";
//
//    String filter4Name = "filter4name";
//
//    String filter5Name = "filter5name";
//
//    String filter6Name = "filter6name";
//
//    String entry1Name = "entry1name";
//
//    String entry2Name = "entry2Annotation";
//
//    String keyValue = "key value";
//
//    String groupValue = SimpleCorrectModel.group;
//
//    String descValue = "descripion value";
//
//    String filter1Value = "filter 1 value";
//
//    String filter2Value = "filter 2 value";
//
//    String filter3Value = "filter 3 value";
//
//    Long filter4Value = 4L;
//
//    Long filter5Value = 5L;
//
//    Long filter6Value = 6L;
//
//    String entry1Value = "entry 1 value";
//
//    String entry2Value = "entry 2 value";
//
//    @Before
//    public void before() throws Exception {
//        simpleCorrectModel = new SimpleCorrectModel(keyValue);
//        simpleCorrectModel.setDesc(descValue);
//        simpleCorrectModel.setFilter1name(filter1Value);
//        simpleCorrectModel.setFilter2name(filter2Value);
//        simpleCorrectModel.setFilter3name(filter3Value);
//        simpleCorrectModel.setFilter4name(filter4Value);
//        simpleCorrectModel.setFilter5name(filter5Value);
//        simpleCorrectModel.setFilter6name(filter6Value);
//        simpleCorrectModel.setEntry1name(entry1Value);
//        simpleCorrectModel.setEntry2name(entry2Value);
//    }
//
//    @After
//    public void after() {
//    }
//
//    @Test
//    public void annotationParsingSimpleTest() {
//        MasterdataMapperCached simpleMapper = new MasterdataMapperCached(SimpleCorrectModel.class);
//        assertThat(simpleMapper, is(notNullValue()));
//        assertThat(simpleMapper.getModel(), is(notNullValue()));
//
//        assertThat(simpleMapper.getPool(), is(notNullValue()));
//        assertThat(simpleMapper.getPool(), is(pool));
//
//        assertThat(simpleMapper.getKeyField(), is(notNullValue()));
//        assertThat(simpleMapper.getKeyField().getName(), is(keyName));
//
//        assertThat(simpleMapper.getGroupField(), is(notNullValue()));
//        assertThat(simpleMapper.getGroupField().getName(), is(groupName));
//
//        assertThat(simpleMapper.getDescField(), is(notNullValue()));
//        assertThat(simpleMapper.getDescField().getName(), is(descName));
//
//        assertThat(simpleMapper.getFilter1Field(), is(notNullValue()));
//        assertThat(simpleMapper.getFilter1Field().getName(), is(filter1Name));
//
//        assertThat(simpleMapper.getFilter2Field(), is(notNullValue()));
//        assertThat(simpleMapper.getFilter2Field().getName(), is(filter2Name));
//
//        assertThat(simpleMapper.getFilter3Field(), is(notNullValue()));
//        assertThat(simpleMapper.getFilter3Field().getName(), is(filter3Name));
//
//        assertThat(simpleMapper.getFilter4Field(), is(notNullValue()));
//        assertThat(simpleMapper.getFilter4Field().getName(), is(filter4Name));
//
//        assertThat(simpleMapper.getFilter5Field(), is(notNullValue()));
//        assertThat(simpleMapper.getFilter5Field().getName(), is(filter5Name));
//
//        assertThat(simpleMapper.getFilter6Field(), is(notNullValue()));
//        assertThat(simpleMapper.getFilter6Field().getName(), is(filter6Name));
//
//        assertThat(simpleMapper.getEntryFieldMap().size(), is(2));
//        assertThat(simpleMapper.getEntryFieldMap(), hasKey(entry1Name));
//        assertThat(simpleMapper.getEntryFieldMap().get(entry1Name).getName(), is(entry1Name));
//        assertThat(simpleMapper.getEntryFieldMap(), hasKey(entry2Name));
//    }
//
//    @Test
//    public void fieldGetHandleSimpleTest() {
//        MasterdataMapperCached simpleMapper = new MasterdataMapperCached(SimpleCorrectModel.class);
//        assertThat(simpleMapper, is(notNullValue()));
//
//        assertThat(simpleMapper.getPool(simpleCorrectModel), is(pool));
//        assertThat(simpleMapper.getKey(simpleCorrectModel), is(keyValue));
//        assertThat(simpleMapper.getGroup(simpleCorrectModel), is(groupValue));
//        assertThat(simpleMapper.getDescription(simpleCorrectModel), is(descValue));
//        assertThat(simpleMapper.getFilter1(simpleCorrectModel), is(filter1Value));
//        assertThat(simpleMapper.getFilter2(simpleCorrectModel), is(filter2Value));
//        assertThat(simpleMapper.getFilter3(simpleCorrectModel), is(filter3Value));
//        assertThat(simpleMapper.getFilter4(simpleCorrectModel), is(filter4Value));
//        assertThat(simpleMapper.getFilter5(simpleCorrectModel), is(filter5Value));
//        assertThat(simpleMapper.getFilter6(simpleCorrectModel), is(filter6Value));
//
//    }
//
//    @Test
//    public void fieldGetHandleEntriesSimpleTest() {
//        MasterdataMapperCached simpleMapper = new MasterdataMapperCached(SimpleCorrectModel.class);
//        assertThat(simpleMapper, is(notNullValue()));
//
//        List<MovilizerGenericDataContainerEntry> data = simpleMapper.getData(simpleCorrectModel);
//        assertThat(data.size(), is(2));
//
//        MovilizerGenericDataContainerEntry entry1 = null;
//        MovilizerGenericDataContainerEntry entry2 = null;
//        for (MovilizerGenericDataContainerEntry entry : data) {
//            if (entry1Name.equals(entry.getName())) {
//                entry1 = entry;
//            }
//
//            if (entry2Name.equals(entry.getName())) {
//                entry2 = entry;
//            }
//        }
//        assertThat(entry1, is(notNullValue()));
//        assertThat(entry1.getValstr(), is(entry1Value));
//        assertThat(entry2, is(notNullValue()));
//        assertThat(entry2.getValstr(), is(entry2Value));
//    }
}
