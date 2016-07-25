package com.movlizer.connector.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.movilizer.connector.service.GeneratedImagesMDInitializer;
import com.movilizer.connector.service.client.MovilizerClient;
import com.movlizer.connector.FullTestConfig;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {FullTestConfig.class})
@PropertySource("classpath:/com/movilizer/modules/bottler/app.properties")
@IntegrationTest
public class GenerateImageMDTest extends TestCase
{

  @Autowired
  GeneratedImagesMDInitializer serviceMDCreator;

  @Autowired
  MovilizerClient serviceMov;

  @Test
  public void testSendMasterDataFromDir()
  {
    serviceMDCreator.sendMasterDataClasspath("masterdata");
    serviceMov.perfomSyncToCloud();
  }

}