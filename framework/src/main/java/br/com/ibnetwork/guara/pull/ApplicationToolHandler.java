package br.com.ibnetwork.guara.pull;

import br.com.ibnetwork.guara.rundata.RunData;

public interface ApplicationToolHandler
{
    String getScope();
    
    String getName();

    ApplicationTool newToolInstance()
    	throws Exception;

    ApplicationTool newToolInstance(RunData data)
    	throws Exception;
}
