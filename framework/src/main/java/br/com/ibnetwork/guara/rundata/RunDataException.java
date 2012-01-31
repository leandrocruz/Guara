package br.com.ibnetwork.guara.rundata;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class RunDataException 
	extends NestableRuntimeException 
{
	public RunDataException(String message, Throwable t) 
	{
		super(message,t);
	}
}
