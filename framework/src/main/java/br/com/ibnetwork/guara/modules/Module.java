package br.com.ibnetwork.guara.modules;

import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.xingu.template.Context;


/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public interface Module
{
    Outcome doPerform(RunData data, Context context)
		throws Exception;

    String getName();

    void setName(String name);
    
    boolean isThreadSafe();

	void setThreadSafe(boolean isThreadSafe);
}
