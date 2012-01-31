package br.com.ibnetwork.guara.modules.actions.test;

import br.com.ibnetwork.guara.Guara;
import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleSupport;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.xingu.template.Context;

public class GetRealPath
    extends ModuleSupport
    implements Module
{

    public Outcome doPerform(RunData data, Context ctx) 
        throws Exception
    {
        String fileName = data.getParameters().get("fileName");
        ctx.put("fileName",fileName);
        ctx.put("path",Guara.getRealPath(fileName));
        return Outcome.UNKNOWN;
    }

}
