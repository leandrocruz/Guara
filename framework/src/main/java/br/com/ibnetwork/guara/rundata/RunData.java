package br.com.ibnetwork.guara.rundata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ibnetwork.guara.message.SystemMessage;
import br.com.ibnetwork.guara.parameters.Parameters;
import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.xingu.template.Context;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public interface RunData
{
    HttpServletRequest getRequest();
    
    HttpServletResponse getResponse();
    
    Parameters getParameters();
    
    PageInfo getPageInfo();
    
    Pipeline getPipeline();
    
    Context getContext();
 
    Outcome getOutcome();
    
    void setOutcome(Outcome outcome);
    
    boolean hasMessage();
    
    SystemMessage getMessage();
    
    String getMessageAsText();
    
    String getMessageAsText(Object[] params);
    
    void setMessage(SystemMessage message);
    
    void setMessage(SystemMessage message, Throwable throwable);
    
    void setMessage(SystemMessage message, Object[] params);
    
    void setMessage(SystemMessage message, Object[] params, Throwable throwable);

    public void setError(Throwable throwable);
    
    public Throwable getError();
}
