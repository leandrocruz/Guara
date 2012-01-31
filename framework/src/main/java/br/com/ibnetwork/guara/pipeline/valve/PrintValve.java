package br.com.ibnetwork.guara.pipeline.valve;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class PrintValve
	extends ValveSupport
{
    public boolean execute(RunData runData)
            throws PipelineException
    {
        System.out.println("Executing valve("+this+"): " + name);
        return true;
    }

}
