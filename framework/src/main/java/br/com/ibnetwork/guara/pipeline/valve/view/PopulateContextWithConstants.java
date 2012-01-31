package br.com.ibnetwork.guara.pipeline.valve.view;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.xingu.template.Context;

/**
 * @author leandro
 */
public class PopulateContextWithConstants 
	extends ValveSupport
{
    private static final String DATA = "data";
	private static final String SESSION = "session";
	private static final String PARAMETERS = "parameters";
	private static final String PAGE_INFO = "pageInfo";

	public boolean execute(RunData data) 
    	throws PipelineException
    {
        Context ctx = data.getContext();
        ctx.put(DATA,data);
        ctx.put(SESSION,data.getRequest().getSession());
        ctx.put(PARAMETERS,data.getParameters());
        ctx.put(PAGE_INFO,data.getPageInfo());
        return true;
    }

}
