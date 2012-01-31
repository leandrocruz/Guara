package br.com.ibnetwork.guara.pipeline.valve;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class CheckSecurityValve
	extends ValveSupport
{

    public boolean execute(RunData runData)
    	throws PipelineException
    {
        System.out.println("Checking security");
        return true;
    }

}
