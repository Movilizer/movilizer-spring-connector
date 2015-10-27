package com.movlizer.connector.v12.spikes;

import com.movilitas.movilizer.v12.MovilizerMovelet;
import com.movilizer.connector.java.jobs.PollingJob;
import com.movlizer.connector.v12.config.MovilizerV12TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Tests for queries retrieving movilizer users.
 *
 * @author Jesús de Mula Cano <jesus.demula@movilizer.com>
 * @version 0.1-SNAPSHOT, 2014.11.10
 * @since 1.0
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@SpringApplicationConfiguration(classes = {MovilizerV12TestConfig.class})
public class MoveletTest {
/*
    private final String moveletXmlPath = "/test-movelets/test-movelet-simple.mxml";

    private final UUID moveletKey = UUID.fromString("b454f5ea-9ee9-49af-b38a-662823aef69b");

    private final String moveletKeyExtension = "";

    private final String moveletName = "Simple movelet";

    @Autowired
    private PollingJob movilizer;

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() {
    }

    @Test
    public void testCreateMovelet() throws Exception {
        MovilizerMovelet movelet = movilizer.unmarshallMoveletFromFile(moveletXmlPath);
        movilizer.createMovelet(movelet);
        movilizer.perfomSyncToCloud();
    }

    @Test
    public void testRemoveMovelet() throws Exception {
        movilizer.removeMovelet(String.valueOf(moveletKey), moveletKeyExtension, true);
        movilizer.perfomSyncToCloud();
    }
    */
}
