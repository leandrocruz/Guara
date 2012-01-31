package br.com.ibnetwork.guara;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class GuaraException
	extends NestableRuntimeException
{

    public GuaraException(String message)
    {
        super(message);
    }

    public GuaraException(String message, Throwable t)
    {
        super(message,t);
    }

}
