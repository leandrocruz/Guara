package br.com.ibnetwork.guara.modules;

import org.apache.commons.lang.exception.NestableException;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class ModuleLoaderException
	extends NestableException
{

    public ModuleLoaderException(String message)
    {
        super(message);
    }

    public ModuleLoaderException(String message, Throwable t)
    {
        super(message,t);
    }
}
