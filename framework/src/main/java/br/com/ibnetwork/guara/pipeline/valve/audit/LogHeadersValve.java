package br.com.ibnetwork.guara.pipeline.valve.audit;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author leandro
 */
public class LogHeadersValve
	extends ValveSupport
{
    public boolean execute(RunData data) 
    	throws PipelineException
    {
        HttpServletRequest req = data.getRequest();
        Enumeration names = req.getHeaderNames();
        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            Enumeration values = req.getHeaders(name);
            while (values.hasMoreElements())
            {
                String value = (String) values.nextElement();
                log.info("Request Header: "+name+" = "+value);    
            }
        }
        return true;
    }
}
