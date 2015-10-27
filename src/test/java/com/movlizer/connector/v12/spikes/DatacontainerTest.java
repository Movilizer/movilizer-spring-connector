package com.movlizer.connector.v12.spikes;


import com.movilitas.movilizer.v12.MovilizerMovelet;
import com.movilitas.movilizer.v12.MovilizerParticipant;
import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
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

import java.util.List;
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
public class DatacontainerTest {
/*
    private final String moveletXmlPath = "/test-movelets/test-movelet-datacontainer-reply.mxml";

    private final UUID moveletKey = UUID.fromString("d554f5ea-9ee9-49af-b38a-662823aef69b");

    private final String moveletKeyExtension = "";

    @Autowired
    private PollingJob movilizer;

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
        MovilizerMovelet movelet = movilizer.unmarshallMoveletFromFile(moveletXmlPath);
        movilizer.createMovelet(movelet);
        movilizer.assignMoveletToParticipant(String.valueOf(moveletKey), participant1);
        movilizer.perfomSyncToCloud();
        Thread.sleep(30 * 1000);
    }

    @Test
    public void test2CheckForDatacontainers() throws Exception {
        movilizer.perfomSyncToCloud();
        List<MovilizerUploadDataContainer> datacontainers = movilizer.getAllDataContainers();
        Thread.sleep(30 * 1000);
    }

    @Test
    public void test3RemoveMovelet() throws Exception {
        movilizer.removeMovelet(String.valueOf(moveletKey), moveletKeyExtension, true);
        movilizer.perfomSyncToCloud();
        Thread.sleep(30 * 1000);
    }
    */
}
