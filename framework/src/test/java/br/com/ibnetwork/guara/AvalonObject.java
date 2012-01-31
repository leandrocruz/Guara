package br.com.ibnetwork.guara;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

public class AvalonObject
	implements Configurable
{
    private Configuration conf;

    private boolean isConfigured;
    
    public void configure(Configuration conf)
    	throws ConfigurationException
    {
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
