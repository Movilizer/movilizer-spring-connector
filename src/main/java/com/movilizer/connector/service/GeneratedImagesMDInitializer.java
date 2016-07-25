package com.movilizer.connector.service;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.movilitas.movilizer.v14.MovilizerRequest;
import com.movilizer.connector.service.client.MovilizerClient;

@Service
public class GeneratedImagesMDInitializer
{

	private final Log logger = LogFactory.getLog(getClass());

  @Autowired
  private OXMUtility serviceXML;

  @Autowired
  private MovilizerClient serviceMov;

  @Autowired
  ResourcePatternResolver resourceResolver;

  public void init()
  {
    sendMasterDataClasspath("masterdata");
  }

  public void sendMasterDataClasspath(String path)
  {
    try
    {
      for (Resource resource : resourceResolver.getResources("classpath:" + path + "/*.mxml"))
      {
        MovilizerRequest request = serviceXML.inputStreamToObject(resource.getInputStream(),
          MovilizerRequest.class);
        serviceMov.updateMasterdata(request.getMasterdataPoolUpdate());
      }
    }
    catch (IOException e)
    {
      logger.error("Problems while reading from classpath directory: " + path, e);
    }
  }
}
