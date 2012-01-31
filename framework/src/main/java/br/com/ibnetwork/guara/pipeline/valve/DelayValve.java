package br.com.ibnetwork.guara.pipeline.valve;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.rundata.RunData;

public class DelayValve
    extends ValveSupport
    implements Configurable
{
    int delay = 3000;
    
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        delay = conf.getChild("delay").getAttributeAsInteger("miliseconds",3000);
    }

    protected boolean execute(RunData data) 
        throws PipelineException
    {
        try
        {
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
