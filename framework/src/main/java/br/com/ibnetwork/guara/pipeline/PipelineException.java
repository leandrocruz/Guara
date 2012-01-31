package br.com.ibnetwork.guara.pipeline;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class PipelineException
	extends NestableRuntimeException
{
    public PipelineException(String message, Throwable t)
    {
        super(message,t);
    }
}
