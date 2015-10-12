package com.movlizer.connector.bpost;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.movilizer.connector.bpost.BPostConnector;
import com.movlizer.connector.FullTestConfig;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { FullTestConfig.class })
@IntegrationTest
public class BPostConnectorTest extends TestCase {

    @Autowired
    BPostConnector bpostConnector;


    @Test
    public void testRun() {
    	bpostConnector.runFullConfig();
    }
}