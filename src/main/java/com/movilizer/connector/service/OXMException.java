package com.movilizer.connector.service;

public class OXMException extends RuntimeException
{

	  public OXMException(String message)
	  {
	    super(message);
	  }

	  public OXMException(Throwable throwable)
	  {
	    super(throwable);
	  }

	  public OXMException(String message, Throwable throwable)
	  {
	    super(message, throwable);
	  }
	}

