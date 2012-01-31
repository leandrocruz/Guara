package br.com.ibnetwork.guara.pipeline.valve;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.Valve;
import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class TestValve
	extends ValveSupport
	implements Valve, Serviceable, Configurable
{
    protected boolean executed;
    
    private boolean composed;

    private Configuration conf;

    private boolean isConfigured;
    
    public boolean execute(RunData runData)
            throws PipelineException
    {
        executed = true;
        return true;
    }

    public boolean hasExecuted()
    {
        return executed;
    }

    public void service(ServiceManager manager) 
    	throws ServiceException
    {
        composed = true;
    }
    
    public boolean isComposed()
    {
        return composed;
    }

    public void configure(Configuration conf)
    	throws ConfigurationException
    {
        //this method will be called by the PipelineFactoryImpl, not the default Factory impl
        //thats thw way we can provide different configurations for the same valve type
        this.conf = conf;
        this.isConfigured = true;
    }

    public Configuration getConfiguration()
    {
        return conf;
    }
    
    public boolean isConfigured()
    {
        return isConfigured;
    }
}
