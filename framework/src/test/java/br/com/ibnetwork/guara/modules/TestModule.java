package br.com.ibnetwork.guara.modules;

import br.com.ibnetwork.guara.AvalonObject;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import xingu.template.Context;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class TestModule
	extends AvalonObject
	implements Module 
{
    private String name;
    
    public Outcome doPerform(RunData runData, Context context)
    	throws Exception
    {
        return Outcome.UNKNOWN;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

	public boolean isThreadSafe()
    {
	    return false;
    }

	public void setThreadSafe(boolean isThreadSafe)
    {
    }
}
