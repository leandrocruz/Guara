package br.com.ibnetwork.guara.modules.actions.test;

import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleSupport;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import xingu.template.Context;

public class ActionTest
    extends ModuleSupport
    implements Module
{
    public Outcome doPerform(RunData data, Context context) 
        throws Exception
    {
        context.put("methodExecuted","doPerform");
        return Outcome.UNKNOWN;
    }
    
    public Outcome doSomething(RunData data, Context context) 
        throws Exception
    {
        context.put("methodExecuted","doSomething");
        return Outcome.UNKNOWN;
    }
    
    public Outcome doOutcome(RunData data, Context context) 
    	throws Exception 
    {
    	context.put("methodExecuted","doOutcome");
        String code = data.getParameters().getString("code");
        String method = data.getParameters().getString("method");
        if("SUCCESS".equals(code))
        {
        	return Outcome.success(this,method);
        }
        else if("ERROR".equals(code))
        {
        	return Outcome.error(this,method);
        }
        else if(code != null)
        {
            return Outcome.unknown(code,this,method);
        }
        else
        {
            return Outcome.UNKNOWN;
        }
    }
}
