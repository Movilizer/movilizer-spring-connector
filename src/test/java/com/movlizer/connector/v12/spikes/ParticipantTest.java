package com.movlizer.connector.v12.spikes;

import com.movilitas.movilizer.v12.MovilizerMovelet;
import com.movilitas.movilizer.v12.MovilizerParticipant;
import com.movilizer.connector.v12.model.PasswordTypes;
import com.movilizer.connector.v12.service.controller.MovilizerCloudInterfaceV12;
import com.movlizer.connector.v12.config.MovilizerV12TestConfig;
import org.jasypt.digest.StandardStringDigester;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringApplicationConfiguration(classes = {MovilizerV12TestConfig.class})
public class ParticipantTest {

    private final String moveletXmlPath = "/test-movelets/test-movelet-simple.mxml";

    private final UUID moveletKey = UUID.fromString("b454f5ea-9ee9-49af-b38a-662823aef69b");

    private final String moveletKeyExtension = "";

    @Autowired
    private MovilizerCloudInterfaceV12 movilizer;

    private MovilizerParticipant participant1;

    private MovilizerParticipant participant2;

    private MovilizerParticipant participant3;

    @Before
    public void before() throws Exception {
        participant1 = new MovilizerParticipant();
        participant1.setParticipantKey("29252932");
        participant1.setName("Test participant 1 assigned for Salesforce Copec");
        participant1.setDeviceAddress("+9991986");

        participant2 = new MovilizerParticipant();
        participant2.setParticipantKey("231756155");
        participant2.setName("Test participant 2 assigned for Salesforce Copec");
        participant2.setDeviceAddress("+9991986001");

        participant3 = new MovilizerParticipant();
        participant3.setParticipantKey("1768069626");
        participant3.setName("Roberto test");
        participant3.setDeviceAddress("+9981100005");

    }

    @After
    public void after() {
    }

    @Test
    public void test1CreateMovelet() throws Exception {
        MovilizerMovelet movelet = movilizer.unmarshallMoveletFromFile(moveletXmlPath);
        movilizer.createMovelet(movelet);
        movilizer.perfomSyncToCloud();
        Thread.sleep(30 * 1000); //Too fast consecutive requests can be ignored by the cloud
    }

    @Test
    public void test2AssignToParticpant() throws Exception {
        movilizer.assignMoveletToParticipant(String.valueOf(moveletKey), participant3);
        movilizer.perfomSyncToCloud();
        Thread.sleep(30 * 1000); //Too fast consecutive requests can be ignored by the cloud
    }

    @Test
    public void test3AssignToSeveralParticpants() throws Exception {
        movilizer.assignMoveletToParticipant(String.valueOf(moveletKey), participant2);
        movilizer.assignMoveletToParticipant(String.valueOf(moveletKey), participant3);
        movilizer.perfomSyncToCloud();
        Thread.sleep(30 * 1000); //Too fast consecutive requests can be ignored by the cloud
    }

    @Test
    public void test4UnAssignToSeveralParticpants() throws Exception {
        movilizer.unassignMoveletToParticipant(String.valueOf(moveletKey), participant1);
        movilizer.unassignMoveletToParticipant(String.valueOf(moveletKey), participant2);
        movilizer.unassignMoveletToParticipant(String.valueOf(moveletKey), participant3);
        movilizer.perfomSyncToCloud();
        Thread.sleep(30 * 1000); //Too fast consecutive requests can be ignored by the cloud
    }

    @Test
    public void test5ResetParticpant() throws Exception {
        movilizer.resetParticipant(participant3.getDeviceAddress());
        movilizer.perfomSyncToCloud();
        Thread.sleep(30 * 1000); //Too fast consecutive requests can be ignored by the cloud
    }

    @Test
    public void test6AssignToParticpantWithTextPassword() throws Exception {
        movilizer.assignPasswordToParticipant(participant3, PasswordTypes.PLAIN_TEXT_PASSWORD, "awf123");
        movilizer.perfomSyncToCloud();

        Thread.sleep(30 * 1000); //Too fast consecutive requests can be ignored by the cloud
    }

    @Test
    public void test7AssignToParticpantWithSHAPassword() throws Exception {
        StandardStringDigester encrypter = new StandardStringDigester();
        encrypter.setAlgorithm("SHA-512");
        encrypter.setIterations(1000);
        encrypter.setSaltSizeBytes(16);
        encrypter.initialize();
        String encrypterdPassword = encrypter.digest("movilizer2");

        movilizer.assignPasswordToParticipant(participant3, PasswordTypes.SHA_512_PASSWORD, encrypterdPassword);
        movilizer.perfomSyncToCloud();

        Thread.sleep(30 * 1000); //Too fast consecutive requests can be ignored by the cloud
    }
}
