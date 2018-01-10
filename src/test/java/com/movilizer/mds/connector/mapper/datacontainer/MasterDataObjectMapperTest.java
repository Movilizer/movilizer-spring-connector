package com.movilizer.mds.connector.mapper.datacontainer;

import com.movilitas.movilizer.v15.MovilizerGenericDataContainerEntry;
import com.movilitas.movilizer.v15.MovilizerMasterdataUpdate;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.mapper.datacontainer.models.MapperTestObject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MasterDataObjectMapperTest {

    private MapperTestObject obj1;
    private MapperTestObject obj2;
    private GenericDataContainerMapper mapper;

    @Mock
    private MovilizerMetricService metricService;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        mapper = new GenericDataContainerMapperImpl(metricService);

        obj1 = MapperTestObject.createTestObject();
        obj2 = MapperTestObject.createTestObject();
    }

    @Test
    public void convertObjectToMD() throws ClassNotFoundException {
        List<MapperTestObject> instances = new ArrayList<>();
        instances.add(obj1);
        instances.add(obj2);

        //create masterdata
        MovilizerMasterdataUpdate movilizerMasterdataUpdate = new MovilizerMasterdataUpdate();
        movilizerMasterdataUpdate.setData(mapper.toDataContainer(instances));
        movilizerMasterdataUpdate.setKey("test key");
        movilizerMasterdataUpdate.setDescription("test desc");

        //extract data and check
        List<MovilizerGenericDataContainerEntry> masterdata = movilizerMasterdataUpdate.getData().getEntry();
        assertThat(masterdata.size(), is(2));

        MovilizerGenericDataContainerEntry entry1 = masterdata.get(0);
        assertThat(entry1.getEntry().size(), is(7));

        assertThat(getEntry(entry1.getEntry(), "stringField").getValstr(), is("test"));
        assertThat(getEntry(entry1.getEntry(), "intField").getValstr(), is("100"));
        assertThat(getEntry(entry1.getEntry(), "booleanField").getValstr(), is("true"));
        assertThat(getEntry(entry1.getEntry(), "dateField").getValstr(), is("2015-01-01"));

        MovilizerGenericDataContainerEntry objectField = getEntry(entry1.getEntry(), "objectField");
        assertThat(getEntry(objectField.getEntry(), "stringField").getValstr(), is("test"));

        MovilizerGenericDataContainerEntry objectFieldList = getEntry(entry1.getEntry(), "objectFieldList");
        MovilizerGenericDataContainerEntry objectFieldListFirstEntry = objectFieldList.getEntry().get(0);
        assertThat(getEntry(objectFieldListFirstEntry.getEntry(), "stringField").getValstr(), is("test"));

    }

    public MovilizerGenericDataContainerEntry getEntry(List<MovilizerGenericDataContainerEntry> updates, String key) {
        for (MovilizerGenericDataContainerEntry entry : updates) {
            if (entry.getName().equals(key)) {
                return entry;
            }
        }
        return null;
    }

}
