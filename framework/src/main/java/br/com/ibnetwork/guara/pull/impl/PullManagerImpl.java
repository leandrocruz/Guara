package br.com.ibnetwork.guara.pull.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.guara.pull.ApplicationTool;
import br.com.ibnetwork.guara.pull.ApplicationToolHandler;
import br.com.ibnetwork.guara.pull.PullManager;
import xingu.container.Inject;
import xingu.factory.Factory;

public class PullManagerImpl 
	implements PullManager, Configurable
{
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Inject
    private Factory factory;
    
    private Map<String, List<ApplicationToolHandler>> toolHandlersByScope = new HashMap<String, List<ApplicationToolHandler>>(5);

    public void configure(Configuration conf) 
    	throws ConfigurationException
    {
        Configuration[] toolsConf = conf.getChild("tools").getChildren("tool");
        for (int i = 0; i < toolsConf.length; i++)
        {
            Configuration toolConfig = toolsConf[i];
            String name = toolConfig.getAttribute("name");
            String scope = toolConfig.getAttribute("scope");
            logger.info("Adding tool name["+name+"] scope ["+scope+"]");
            ApplicationToolHandler handler = createToolHandler(toolConfig);
            addToRegistry(handler);
        }
    }

    private ApplicationToolHandler createToolHandler(Configuration conf) 
    	throws ConfigurationException
    {
        return new ApplicationToolHandlerImpl(conf,factory);
    }

    private void addToRegistry(ApplicationToolHandler handler)
    {
        String scope = handler.getScope();
        List<ApplicationToolHandler> handlersOnScope = toolHandlersByScope.get(scope);
        if(handlersOnScope == null)
        {
            handlersOnScope = new ArrayList<ApplicationToolHandler>();
            toolHandlersByScope.put(scope,handlersOnScope);
        }
        handlersOnScope.add(handler);
    }

    public List<ApplicationToolHandler> getAllToolHandlers(String scope)
    {
        return toolHandlersByScope.get(scope);
    }

    @Override
    public ApplicationTool toolByName(String name)
        throws Exception
    {
        for(String scope : toolHandlersByScope.keySet())
        {
            List<ApplicationToolHandler> tools = toolHandlersByScope.get(scope);
            for (ApplicationToolHandler handler : tools)
            {
                if(handler.getName().equals(name))
                {
                    return handler.newToolInstance();
                }
            }
            
        }
        return null;
    }
}
