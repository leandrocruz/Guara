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
public class AbortValve
	extends ValveSupport
	implements Valve, Serviceable, Configurable
{
    protected boolean executed;
    
    public boolean execute(RunData runData)
            throws PipelineException
    {
        executed = true;
        return false;
    }

    public boolean hasExecuted()
    {
        return executed;
    }

    public void service(ServiceManager manager) 
        throws ServiceException
    {
    }

    public void configure(Configuration conf) 
        throws ConfigurationException
    {
    }
}
