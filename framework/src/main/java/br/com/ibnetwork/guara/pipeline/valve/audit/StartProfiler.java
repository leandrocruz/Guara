package br.com.ibnetwork.guara.pipeline.valve.audit;


import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.RunData;

public class StartProfiler
    extends ValveSupport
    implements Configurable
{
    private String label;
    
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        label = conf.getChild("profiler").getAttribute("label");
    }

    protected boolean execute(RunData data) 
        throws PipelineException
    {
        Monitor global = MonitorFactory.start(label);
        data.getContext().put(label,global);
        return true;
    }
}
