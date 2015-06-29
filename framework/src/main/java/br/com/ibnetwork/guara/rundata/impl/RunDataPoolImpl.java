package br.com.ibnetwork.guara.rundata.impl;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.parameters.ParameterParserBuilder;
import br.com.ibnetwork.guara.parameters.Parameters;
import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.PipelineManager;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.PageInfo;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.rundata.RunDataException;
import br.com.ibnetwork.guara.rundata.RunDataPool;
import br.com.ibnetwork.guara.util.ServletUtils;
import xingu.container.Inject;
import xingu.template.Context;
import xingu.template.TemplateEngine;

public class RunDataPoolImpl
    implements RunDataPool
{
    private Log logger = LogFactory.getLog(getClass());
    
    @Inject
    private ParameterParserBuilder parserBuilder;
    
    @Inject
    private PipelineManager pipelineManager;
    
    @Inject
    private TemplateEngine templateEngine;

    public RunData createFrom(HttpServletRequest request, HttpServletResponse response, ServletConfig servletConfig)
    	throws Exception
    {
        RunDataImpl data = new RunDataImpl();

        //request and response
        data.setHttpServletRequest(request);
        data.setHttpServletResponse(response);

        //parameters
        Parameters params = parserBuilder.createParameterParser(request);
        data.setParameterParser(params);

        //page info
        PageInfo pageInfo = null;
        try
        {
            pageInfo = createPageInfo(request,params);
            data.setPageInfo(pageInfo);
        } 
        catch(Exception e)
        {
        	String url = ServletUtils.getURLFromRequest(request);
        	throw new RunDataException("Error handling url: "+url,e);
        }

        if(logger.isDebugEnabled())
        {
            logger.debug("template is ["+pageInfo.getTemplate()+"]");
            logger.debug("layoutTemplate is ["+pageInfo.getLayoutTemplate()+"]");
            logger.debug("screenName is ["+pageInfo.getScreenName()+"]");
            logger.debug("actionName is ["+pageInfo.getActionName()+"]");
        }
        
        //pipeline
        String pipelineName = params.get("pipeline");
        if(pipelineName != null)
        {
            Pipeline pipeline = pipelineManager.getPipeline(pipelineName);
            data.setPipeline(pipeline);
        }
        
        //context
        Context ctx = templateEngine.createContext();
        data.setContext(ctx);
        
        //outcome
        data.setOutcome(Outcome.UNKNOWN);

        return data;
    }

    private PageInfo createPageInfo(HttpServletRequest request, Parameters pp)
    {
        PageInfo pageInfo = new PageInfo();
        
        String screenTemplate = pp.getTrimmed(PageInfo.TEMPLATE);
        pageInfo.setTemplate(screenTemplate);
        
        String screenName = pp.getTrimmed(PageInfo.SCREEN);
        pageInfo.setScreenName(screenName);

        String layoutTemplate = pp.getTrimmed(PageInfo.LAYOUT);
        pageInfo.setLayoutTemplate(layoutTemplate);
        
        String actionName = pp.getTrimmed(PageInfo.ACTION);
        pageInfo.setActionName(actionName);

        return pageInfo;
    }
}
