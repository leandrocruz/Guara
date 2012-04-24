package br.com.ibnetwork.guara.rundata.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ibnetwork.guara.message.SystemMessage;
import br.com.ibnetwork.guara.parameters.Parameters;
import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.rundata.PageInfo;
import xingu.template.Context;

/**
 * @author leandro
 */
public class RunDataImpl
    implements RunData
{
    
    private static final String OUTCOME = "outcome";

	private HttpServletRequest request;
    
    private HttpServletResponse response;
    
    private Parameters parameters;
    
    private PageInfo pageInfo;
    
    private Pipeline pipeline;
    
    private Context context;
    
    private SystemMessage message;

    private Object[] messageParams;

    private Throwable error;
    
    private Outcome outcome;
    
    public HttpServletRequest getRequest()
    {
        return request;
    }
    
    public void setHttpServletRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

    public void setHttpServletResponse(HttpServletResponse response)
    {
        this.response = response;
    }
    
    public Parameters getParameters()
    {
        return parameters;
    }

    public void setParameterParser(Parameters parameters)
    {
        this.parameters = parameters;
    }
    
    public PageInfo getPageInfo()
    {
        return pageInfo;
    }
    
    public void setPageInfo(PageInfo pageInfo)
    {
        this.pageInfo = pageInfo;
    }

    public Pipeline getPipeline()
    {
        return pipeline;
    }
    
    public void setPipeline(Pipeline pipeline)
    {
        this.pipeline = pipeline;
    }
    
    public Context getContext()
    {
        return context;
    }
    
    public void setContext(Context context)
    {
        this.context = context;
    }

    public Outcome getOutcome() {
		return outcome;
	}

	public void setOutcome(Outcome outcome) {
		context.put(OUTCOME,outcome);
		this.outcome = outcome;
	}

	/* 
	 * Message 
	 */
	
    public boolean hasMessage()
    {
        return message != null;
    }
    
    public SystemMessage getMessage()
    {
        return message;
    }

    public String getMessageAsText()
    {
        String result = message == null ? null : message.getText(messageParams);
        return result;
    }
    
    public String getMessageAsText(Object[] params)
    {
        String result = message == null ? null : message.getText(params);
        return result;
    }

    public void setMessage(SystemMessage message)
    {
        this.message = message;
        this.messageParams = null;
    }

    public void setMessage(SystemMessage message, Throwable t)
    {
        this.message = message;
        this.messageParams = null;
        this.error = t;
    }
    
    public void setMessage(SystemMessage message, Object[] params)
    {
        this.message = message;
        this.messageParams = params;
    }

    public void setMessage(SystemMessage message, Object[] params, Throwable t)
    {
        this.message = message;
        this.messageParams = params;
        this.error = t;
    }
    
    public void setError(Throwable t)
    {
    	this.error = t;
    }
    
    public Throwable getError()
    {
    	return error;
    }
}