package br.com.ibnetwork.guara.pipeline.valve.pull;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.pull.ApplicationToolHandler;
import br.com.ibnetwork.guara.pull.PullManager;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.template.Context;

public class PopulateContextWithTools 
	extends ValveSupport
{
    private Log logger = LogFactory.getLog(PopulateContextWithTools.class);
    
    @Inject
    private PullManager pullManager;

    public boolean execute(RunData runData) 
    	throws PipelineException
    {
        //add global tools - all from cache
        List<ApplicationToolHandler> globalTools = pullManager.getAllToolHandlers(PullManager.SCOPE_GLOBAL);
        populateContext(globalTools, runData);
        
        //add request tools - all new
        List<ApplicationToolHandler> requestTools = pullManager.getAllToolHandlers(PullManager.SCOPE_REQUEST);
        populateContext(requestTools, runData);
        
        //add session tools - all new if session is new
        HttpSession session = runData.getRequest().getSession();
        if(session.isNew())
        {
            List<ApplicationToolHandler> sessionTools = pullManager.getAllToolHandlers(PullManager.SCOPE_SESSION);
            populateContext(sessionTools, runData);
        }
        else
        {
            //TODO: restore tools from session
        }
        return true;
    }

    private void populateContext(List<ApplicationToolHandler> tools, RunData runData)
    {
        if(tools == null)
        {
            return;
        }
        Context ctx = runData.getContext();
        for (ApplicationToolHandler handler : tools)
        {
            try
            {
                Object tool = handler.newToolInstance(runData);
                ctx.put(handler.getName(),tool);
                if(logger.isDebugEnabled())
                {
                    logger.debug("Adding tool["+handler.getName()+"] scope["+handler.getScope()+"] to context");
                }
            }
            catch (Exception e)
            {
                logger.error("Error adding tool to context",e);
            }
            
        }
    }
}
