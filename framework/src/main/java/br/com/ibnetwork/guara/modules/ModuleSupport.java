package br.com.ibnetwork.guara.modules;

import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import xingu.template.Context;

public abstract class ModuleSupport
    implements Module
{
    protected String name;
    
    protected boolean isThreadSafe;
    
    public Outcome doPerform(RunData data, Context context) 
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
		return isThreadSafe;
    }

	public void setThreadSafe(boolean isThreadSafe)
    {
		this.isThreadSafe = isThreadSafe;
    }
}
