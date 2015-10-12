package com.movlizer.connector.bpost;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.movilizer.connector.bpost.AssignmentBoardUtility;
import com.movlizer.connector.FullTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { FullTestConfig.class })
@IntegrationTest
public class AssignmentBoardUtilityTest {

	@Autowired
	AssignmentBoardUtility utility;


	@Test
	public void testPushSotiCSV()
	{
		utility.pushSotiCSV(false);
		
	}
}
