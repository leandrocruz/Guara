package br.com.ibnetwork.guara.pipeline;

public class PipelineException
	extends RuntimeException
{
    public PipelineException(String message, Throwable t)
    {
        super(message,t);
    }
}
