package br.com.ibnetwork.guara.pull.tools;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;

/**
 * @author leandro
 */
public class SkinTool 
	extends ApplicationToolSupport
	implements Configurable, Initializable
{
    private Properties props;
    
    private String fileName;
    
    public void configure(Configuration conf) 
    	throws ConfigurationException
    {
        fileName = conf.getChild("properties").getAttribute("fileName");
    }

    public void initialize() 
    	throws Exception
    {
        props = new Properties();
        InputStream is = new FileInputStream(fileName);
        props.load(is);
    }

    public String get(String key)
    {
        return props.getProperty(key);
    }
}
