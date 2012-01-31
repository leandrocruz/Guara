package br.com.ibnetwork.guara.pipeline.factory;

import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;

import br.com.ibnetwork.guara.pipeline.Pipeline;

public interface PipelineFactory
{
    Map<String, Pipeline> createPipelineMap(Configuration conf)
    	throws Exception;
}
