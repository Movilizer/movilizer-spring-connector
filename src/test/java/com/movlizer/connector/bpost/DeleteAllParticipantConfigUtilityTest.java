package com.movlizer.connector.bpost;

import com.movilizer.connector.bpost.AssignmentBoardUtility;
import com.movlizer.connector.FullTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {FullTestConfig.class})
@IntegrationTest
public class DeleteAllParticipantConfigUtilityTest {

    @Autowired
    AssignmentBoardUtility utility;


    @Test
    public void testDeleteAllParticipantConfiguration() throws InterruptedException {
        String moveletKey = "com.movilizer.bpost.deleteAllTrigger";
        utility.deleteAllParticipantConfigurationTriggeringMAFScript(moveletKey, "/bpost/movelets/deleteAllConfig.mxml", false, 140, 3, 5);
    }
}
