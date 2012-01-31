package br.com.ibnetwork.guara.modules.actions.test;

import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleSupport;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.xingu.template.Context;

public class Slow
    extends ModuleSupport
    implements Module
{

    public Outcome doPerform(RunData data, Context context) 
        throws Exception
    {
        Thread.sleep(100);
        return Outcome.UNKNOWN;
    }

}
