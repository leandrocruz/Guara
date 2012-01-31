package br.com.ibnetwork.guara.message;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class SystemMessageException 
	extends NestableRuntimeException
{
    public SystemMessageException(String message)
    {
		super(message);
    }

}
