package br.com.ibnetwork.guara.pull.tools;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.guara.Guara;
import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.util.ServletUtils;

/**
 * @author leandro
 */
public class ContentTool
	extends ApplicationToolSupport
	implements Configurable
{
    String baseAddress;
    
    String contextName;
    
    public void configure(Configuration conf) 
    	throws ConfigurationException
    {
        Configuration addr = conf.getChild("baseAddress",false);

        /*
    	 * If the baseAddress configuration element is not found, we try to recreate the url using the request object.
    	 * Remember to use the "request" scope for this tool.
    	 */
        if(addr != null)
        {
            String protocol = addr.getAttribute("protocol","http");
            String hostName = addr.getAttribute("hostName","localhost");
            String port = addr.getAttribute("port",null);
            port = port == null || "80".equals(port) ? "" : ":" + port;
            contextName = addr.getAttribute("contextName","/");
            baseAddress = protocol 
            	+ "://"
            	+ hostName
            	+ port
            	+ contextName;
        }
    }

    public void refresh(RunData data)
    {
        if(baseAddress == null)
        {
            HttpServletRequest request = data.getRequest();
            baseAddress = ServletUtils.getBaseAddressFromRequest(request);
            contextName = request.getContextPath();
        }
    }

    public String getURI(String resourceName)
    {
        return baseAddress + resourceName;
    }

    public String getContextName()
    {
        return contextName;
    }

    public String getRealPath(String resourceName)
    {
    	return Guara.getRealPath(resourceName);
    }
    
    public URL getResource(String resourceName)
    {
    	return Guara.getResource(resourceName);
    }
}
