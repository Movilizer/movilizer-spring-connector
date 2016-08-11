package com.movilizer.connector.spikes;

import com.movilitas.movilizer.v14.MovilizerMovelet;
import com.movilitas.movilizer.v14.MovilizerParticipant;
import com.movilitas.movilizer.v14.MovilizerRequest;
import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.MovilizerConnectorAPI;
import com.movilizer.connector.config.MovilizerV12TestConfig;
import com.movilizer.connector.model.MovilizerCallback;
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
public class DatacontainerTest {

    private final String moveletXmlPath = "/test-movelets/test-movelet-datacontainer-reply.mxml";

    private final UUID moveletKey = UUID.fromString("d554f5ea-9ee9-49af-b38a-662823aef69b");

    private final String moveletKeyExtension = "";

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
    public void test1CreateMovelet() throws Exception {
        Path moveletPath = Paths.get(getClass().getResource(moveletXmlPath).toURI());
        MovilizerRequest request = mds.getRequestFromFile(moveletPath);
        if (request.getMoveletSet().size() == 1 && request.getMoveletSet().get(0).getMovelet().size() == 1) {
            MovilizerMovelet movelet = request.getMoveletSet().get(0).getMovelet().get(0);
            movilizerConnector.createMovelet(movelet);
            movilizerConnector.assignMoveletToParticipant(String.valueOf(moveletKey), participant1);
        }
    }

    @Test
    public void test2CheckForDatacontainers() throws Exception {
        movilizerConnector.registerCallback(new MovilizerCallback() {
            @Override
            public void execute() {
                List<MovilizerUploadDataContainer> dcList = movilizerConnector.getAllDataContainers();
                List<String> dcKeys = new ArrayList<>();
                for (MovilizerUploadDataContainer container : dcList) {
                    dcKeys.add(container.getContainer().getKey());
                }
                movilizerConnector.setDataContainersAsProcessed(dcKeys);
            }
        });
    }

    @Test
    public void test3RemoveMovelet() throws Exception {
        movilizerConnector.removeMovelet(String.valueOf(moveletKey), moveletKeyExtension, true);
    }

}
