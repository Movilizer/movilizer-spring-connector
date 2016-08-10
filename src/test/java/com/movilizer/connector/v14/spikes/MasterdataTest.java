package com.movilizer.connector.v14.spikes;

import com.movilitas.movilizer.v14.*;
import com.movilizer.connector.java.MovilizerConnectorAPI;
import com.movilizer.connector.v14.config.MovilizerV12TestConfig;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Tests for queries retrieving movilizer users.
 *
 * @author Jesús de Mula Cano <jesus.demula@movilizer.com>
 * @version 0.1-SNAPSHOT, 2014.11.10
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringApplicationConfiguration(classes = {MovilizerV12TestConfig.class})
public class MasterdataTest {

    private final String moveletXmlPath = "/test-movelets/test-movelet-masterdata.mxml";

    private final UUID moveletKey = UUID.fromString("b3d5c1f6-0db9-44e9-b572-738b4dc94862");

    private final String moveletKeyExtension = "";

    private final String masterdataPool = "SalesforceTestMasterDataPool";

    private final String masterdataGroup = "SalesforceTestMasterDataGroup";

    private final String entryKey = "entryKey";

    private final String entryDesc = "decription";

    private final String entryDataName = "name";

    private final String entryDataVal = "value";

    private final String entryKey2 = "entryKey2";

    private final String entryDesc2 = "decription2";

    private final String entryDataName2 = "name2";

    private final String entryDataVal2 = "value2";

    @Autowired
    private MovilizerConnectorAPI movilizerConnector;
    @Autowired
    private MovilizerDistributionService mds;
    private MovilizerParticipant participant1;

    @Before
    public void before() throws Exception {
        participant1 = new MovilizerParticipant();
        participant1.setParticipantKey("29252932");
        participant1.setName("Test participant 1 assigned for Salesforce Copec");
        participant1.setDeviceAddress("+9991986");
    }

    @After
    public void after() {
    }

    @Test
    public void test1CreateMoveletAndAssignToParticipant() throws Exception {
        Path moveletPath = Paths.get(getClass().getResource(moveletXmlPath).toURI());
        MovilizerRequest request = mds.getRequestFromFile(moveletPath);
        if (request.getMoveletSet().size() == 1 && request.getMoveletSet().get(0).getMovelet().size() == 1) {
            MovilizerMovelet movelet = request.getMoveletSet().get(0).getMovelet().get(0);
            movilizerConnector.createMovelet(movelet);
            movilizerConnector.assignMoveletToParticipant(String.valueOf(moveletKey), participant1);
        }
    }

    @Test
    public void test2CreateMasterdata() throws Exception {
        List<MovilizerMasterdataPoolUpdate> masterdataList = new ArrayList<>();
        MovilizerMasterdataPoolUpdate masterdataPoolUpdate = new MovilizerMasterdataPoolUpdate();
        masterdataPoolUpdate.setPool(masterdataPool);

        MovilizerMasterdataUpdate masterdataUpdate = new MovilizerMasterdataUpdate();
        masterdataUpdate.setKey(entryKey);
        masterdataUpdate.setDescription(entryDesc);
        masterdataUpdate.setGroup(masterdataGroup);
        MovilizerGenericDataContainer dataContainer = new MovilizerGenericDataContainer();
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName(entryDataName);
        entry.setValstr(entryDataVal);
        dataContainer.getEntry().add(entry);
        masterdataUpdate.setData(dataContainer);

        MovilizerMasterdataUpdate masterdataUpdate2 = new MovilizerMasterdataUpdate();
        masterdataUpdate2.setKey(entryKey2);
        masterdataUpdate2.setDescription(entryDesc2);
        masterdataUpdate2.setGroup(masterdataGroup);
        MovilizerGenericDataContainer dataContainer2 = new MovilizerGenericDataContainer();
        MovilizerGenericDataContainerEntry entry2 = new MovilizerGenericDataContainerEntry();
        entry2.setName(entryDataName2);
        entry2.setValstr(entryDataVal2);
        dataContainer2.getEntry().add(entry2);
        masterdataUpdate2.setData(dataContainer2);

        masterdataPoolUpdate.getUpdate().add(masterdataUpdate);
        masterdataPoolUpdate.getUpdate().add(masterdataUpdate2);

        masterdataList.add(masterdataPoolUpdate);
        movilizerConnector.updateMasterdata(masterdataList);
    }

    @Test
    public void test3EditMasterdata() throws Exception {
        List<MovilizerMasterdataPoolUpdate> masterdataList = new ArrayList<>();
        MovilizerMasterdataPoolUpdate masterdataPoolUpdate = new MovilizerMasterdataPoolUpdate();
        masterdataPoolUpdate.setPool(masterdataPool);

        MovilizerMasterdataUpdate masterdataUpdate = new MovilizerMasterdataUpdate();
        masterdataUpdate.setKey(entryKey);
        masterdataUpdate.setDescription(entryDesc + " - updated");
        masterdataUpdate.setGroup(masterdataGroup);
        MovilizerGenericDataContainer dataContainer = new MovilizerGenericDataContainer();
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName(entryDataName + " - updated");
        entry.setValstr(entryDataVal + " - updated");
        dataContainer.getEntry().add(entry);
        masterdataUpdate.setData(dataContainer);

        masterdataPoolUpdate.getUpdate().add(masterdataUpdate);

        masterdataList.add(masterdataPoolUpdate);
        movilizerConnector.updateMasterdata(masterdataList);
    }

    @Test
    public void test4DeleteMasterdataEntry() throws Exception {
        List<MovilizerMasterdataPoolUpdate> masterdataList = new ArrayList<>();
        MovilizerMasterdataPoolUpdate masterdataPoolUpdate = new MovilizerMasterdataPoolUpdate();
        masterdataPoolUpdate.setPool(masterdataPool);

        MovilizerMasterdataDelete masterdataDelete = new MovilizerMasterdataDelete();
        masterdataDelete.setKey(entryKey);

        masterdataPoolUpdate.getDelete().add(masterdataDelete);

        masterdataList.add(masterdataPoolUpdate);
        movilizerConnector.updateMasterdata(masterdataList);
    }

    @Test
    public void test5DeleteMasterdataPool() throws Exception {
        List<MovilizerMasterdataPoolUpdate> masterdataList = new ArrayList<>();
        MovilizerMasterdataPoolUpdate masterdataPoolUpdate = new MovilizerMasterdataPoolUpdate();
        masterdataPoolUpdate.setPool(masterdataPool);

        MovilizerMasterdataDelete masterdataDelete = new MovilizerMasterdataDelete();
        masterdataPoolUpdate.getDelete().add(masterdataDelete);

        masterdataList.add(masterdataPoolUpdate);
        movilizerConnector.updateMasterdata(masterdataList);
    }

    @Test
    public void test6DeleteTestMovelet() throws Exception {
        movilizerConnector.removeMovelet(String.valueOf(moveletKey), moveletKeyExtension, true);
    }

}
