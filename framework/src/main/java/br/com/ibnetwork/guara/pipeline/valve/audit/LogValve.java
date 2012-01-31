package br.com.ibnetwork.guara.pipeline.valve.audit;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.RunData;

public class LogValve
	extends ValveSupport
	implements Configurable
{
    private String message;
    
    public void configure(Configuration conf) 
    	throws ConfigurationException
    {
        message = conf.getChild("message").getAttribute("text");
    }

    public boolean execute(RunData runData) 
    	throws PipelineException
    {
        log.info(message);
        return true;
    }
}
