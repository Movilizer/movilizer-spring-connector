package com.movilizer.connector.spikes;

import com.movilitas.movilizer.v14.MovilizerMovelet;
import com.movilitas.movilizer.v14.MovilizerRequest;
import com.movilizer.connector.MovilizerConnectorAPI;
import com.movilizer.connector.config.MovilizerV12TestConfig;
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
public class MoveletTest {

    private final String moveletXmlPath = "/test-movelets/test-movelet-simple.mxml";

    private final UUID moveletKey = UUID.fromString("b454f5ea-9ee9-49af-b38a-662823aef69b");

    private final String moveletKeyExtension = "";

    private final String moveletName = "Simple movelet";

    @Autowired
    private MovilizerDistributionService mds;
    @Autowired
    private MovilizerConnectorAPI movilizerConnector;

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() {
    }

    @Test
    public void testCreateMovelet() throws Exception {
        Path moveletPath = Paths.get(getClass().getResource(moveletXmlPath).toURI());
        MovilizerRequest request = mds.getRequestFromFile(moveletPath);
        if (request.getMoveletSet().size() == 1 && request.getMoveletSet().get(0).getMovelet().size() == 1) {
            MovilizerMovelet movelet = request.getMoveletSet().get(0).getMovelet().get(0);
            movilizerConnector.createMovelet(movelet);
        }
    }

    @Test
    public void testRemoveMovelet() throws Exception {
        movilizerConnector.removeMovelet(String.valueOf(moveletKey), moveletKeyExtension, true);
    }

}
