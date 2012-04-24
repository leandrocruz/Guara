package br.com.ibnetwork.guara.modules.actions.test;

import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleSupport;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import xingu.template.Context;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class ForceError
    extends ModuleSupport
	implements Module
{

    public Outcome doPerform(RunData runData, Context context)
    	throws Exception
    {
    	throw new Exception("Forced error");
    }

}
