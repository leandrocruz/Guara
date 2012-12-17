package br.com.ibnetwork.guara.pull.impl;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.guara.pull.ApplicationTool;
import br.com.ibnetwork.guara.pull.ApplicationToolHandler;
import br.com.ibnetwork.guara.pull.PullManager;
import br.com.ibnetwork.guara.rundata.RunData;

public class ApplicationToolHandlerImpl 
	implements ApplicationToolHandler
{
    private String name;
    
    private String scope;
    
    private String className;
    
    private Factory factory;
    
    private ApplicationTool instance;
    
    private boolean useCache;

	private Configuration conf;
    
    public ApplicationToolHandlerImpl(Configuration conf, Factory factory) 
    	throws ConfigurationException
    {
    	this.factory = factory;
        this.conf = conf;
		name = conf.getAttribute("name");
        scope = conf.getAttribute("scope");
        className = conf.getAttribute("className");
        useCache = PullManager.SCOPE_GLOBAL.equals(scope); 
    }

    public String getScope()
    {
        return scope;
    }

    public String getName()
    {
        return name;
    }

    
    public ApplicationTool newToolInstance()
		throws Exception
	{
        return newToolInstance(null);
	}
    
    public ApplicationTool newToolInstance(RunData data)
    	throws Exception
    {
        Object obj;
        if(instance == null)
        {
            obj = factory.create(className, conf);
        }
        else
        {
            obj = instance;
        }
        ApplicationTool tool = (ApplicationTool) obj;
        tool.refresh(data);
        if(useCache)
        {
            instance = tool;
        }
        return tool;
    }

	@Override
	public String toString()
	{
		return "tool handler: " + className;
	}
}
