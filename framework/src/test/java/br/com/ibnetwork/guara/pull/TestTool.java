package br.com.ibnetwork.guara.pull;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;

/**
 * @author leandro
 */
public class TestTool 
	extends ApplicationToolSupport
	implements Configurable
{
    private boolean configured;
    
    private Configuration conf;

    public TestTool()
    {
        //ignored
    }
    
    public void configure(Configuration conf) 
    	throws ConfigurationException
    {
        this.configured = true;
        this.conf = conf;
    }

    public Configuration getConfiguration()
    {
        return conf;
    }
    public boolean isConfigured()
    {
        return configured;
    }
}
