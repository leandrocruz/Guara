package br.com.ibnetwork.guara.pipeline.impl;

import java.net.URL;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.PipelineManager;
import br.com.ibnetwork.guara.pipeline.factory.PipelineFactory;
import br.com.ibnetwork.guara.util.ConfigurationLoader;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;

public class PipelineManagerImpl
	implements PipelineManager, Configurable
{
    private Log logger = LogFactory.getLog(getClass());
    
    private String configurationFileName;
    
    @Inject
    private Factory factory;
    
    private PipelineFactory pipelineFactory;
    
    private Map<String, Pipeline> map;

    public void configure(Configuration conf) 
    	throws ConfigurationException
    {
        pipelineFactory = (PipelineFactory) factory.create(PipelineFactory.class);
        configurationFileName = conf.getChild("file").getAttribute("name","pipeline.xml");
    }


    public Map<String, Pipeline> getPipelineMap()
    	throws PipelineException
    {
        if(map != null)
        {
            return map;
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(configurationFileName);
        logger.debug("Loading pipeline data from: " + url);
        try
        {
            Configuration conf = ConfigurationLoader.load(url.getFile());
            map = pipelineFactory.createPipelineMap(conf);
            return map;
        }
        catch (Exception e)
        {
            throw new PipelineException("Error loading pipeline data",e);
        }
    }

    public Pipeline getPipeline(String name) 
    	throws PipelineException
    {
        if(map == null)
        {
            map = getPipelineMap();
        }
        return map.get(name);
    }
    
}
