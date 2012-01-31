package br.com.ibnetwork.guara.pipeline;

import java.util.Map;

public interface PipelineManager
{
    Map<String, Pipeline> getPipelineMap()
    	throws PipelineException;

    Pipeline getPipeline(String name)
    	throws PipelineException;
}
