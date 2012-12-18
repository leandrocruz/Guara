package br.com.ibnetwork.guara.pipeline.valve;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.rundata.UrlRedirector;
import br.com.ibnetwork.xingu.container.Inject;

public class RedirectRequest
    extends ValveSupport
{
    @Inject
    private UrlRedirector redirector;
    
    @Override
    protected boolean execute(RunData data) 
        throws PipelineException
    {
        String location = redirector.locationFor(data);
        if(StringUtils.isEmpty(location))
        {
            return true;
        }
        HttpServletResponse response = data.getResponse();
        response.setStatus(301);
        response.setHeader("Location", location); 
        response.setHeader("Connection", "close"); 
        return false;
    }
}
