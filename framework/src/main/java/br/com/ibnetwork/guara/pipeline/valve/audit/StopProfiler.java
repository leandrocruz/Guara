package br.com.ibnetwork.guara.pipeline.valve.audit;


import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jamonapi.Monitor;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.RunData;

public class StopProfiler
    extends ValveSupport
    implements Configurable
{
    private String label;
    
    private Log logger;
    
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        label = conf.getChild("profiler").getAttribute("label");
        logger = LogFactory.getLog(label);
    }

    protected boolean execute(RunData data) 
        throws PipelineException
    {
        Monitor mon = (Monitor) data.getContext().get(label);
        if(mon != null) 
        {
            mon.stop();
            logger.info(mon);
        }
        return true;
    }

}
