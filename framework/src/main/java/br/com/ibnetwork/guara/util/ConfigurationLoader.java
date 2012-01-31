package br.com.ibnetwork.guara.util;


import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class ConfigurationLoader
{
    private static final DefaultConfigurationBuilder builder = new  DefaultConfigurationBuilder();

    public static Configuration load(String fileName) 
    	throws Exception
    {
        Configuration conf = builder.buildFromFile(fileName);
        return conf;
    }
}
