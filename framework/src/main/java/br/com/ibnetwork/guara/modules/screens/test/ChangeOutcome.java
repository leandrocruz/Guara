package br.com.ibnetwork.guara.modules.screens.test;

import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleSupport;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import xingu.template.Context;

public class ChangeOutcome 
    extends ModuleSupport
	implements Module 
{

	public Outcome doPerform(RunData data, Context context) 
		throws Exception 
	{
		int outcome = data.getParameters().getInt("outcome");
		return null;
	}

}
