package br.com.ibnetwork.guara.pipeline.factory.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.Valve;
import br.com.ibnetwork.guara.pipeline.factory.PipelineFactory;
import br.com.ibnetwork.guara.pipeline.impl.PipelineImpl;
import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.utils.ObjectUtils;


public class PipelineFactoryImpl
	implements PipelineFactory
{
    private Log logger = LogFactory.getLog(getClass());

    @Inject
    private Factory factory;
    
    public Map<String, Pipeline> createPipelineMap(Configuration conf)
    	throws Exception
    {
        Configuration[] pipelines = conf.getChildren("pipeline");
        Map<String, Pipeline> result = new HashMap<String, Pipeline>(pipelines.length);
        for (int i = 0; i < pipelines.length; i++)
        {
            Configuration pipelineConf = pipelines[i];
            Pipeline pipeline = createPipeline(pipelineConf);
            result.put(pipeline.getName(),pipeline);
        }
        return result;
    }

    private Pipeline createPipeline(Configuration conf) 
    	throws Exception
    {
        //TODO: use className
        String name = conf.getAttribute("name",Pipeline.DEFAULT_PIPELINE_NAME);
        logger.debug("Creating pipeline["+name+"]");
        PipelineImpl pipeline = new PipelineImpl(name);
        Configuration[] valves = conf.getChildren("valve");
        for (int i = 0; i < valves.length; i++)
        {
            Configuration valveConf = valves[i];
            Valve valve = createValve(pipeline,valveConf);
            pipeline.addValve(valve);
        }
        return pipeline;
    }

    private Valve createValve(PipelineImpl pipeline, Configuration conf) 
    	throws Exception
    {
        String name = conf.getAttribute("name");
        String className = conf.getAttribute("className");
        logger.debug("Creating valve["+name+"]");
        Valve valve = (Valve) factory.create(ObjectUtils.loadClass(className),conf);
        valve.setPipeline(pipeline);
        valve.setName(name);
        return valve;
    }
}
