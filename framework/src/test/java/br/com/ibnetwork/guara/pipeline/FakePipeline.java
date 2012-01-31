package br.com.ibnetwork.guara.pipeline;

import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class FakePipeline
	implements Pipeline
{

    public String getName()
    {
        return null;
    }

    public void execute(RunData runData)
    	throws PipelineException
    {}

    public Valve[] getValves()
    {
        return null;
    }

    public Valve getValveByName(String valveName)
    {
        return null;
    }

    public void addValve(Valve valve)
    	throws PipelineException
    {}

    public void removeValve(Valve valve)
    	throws PipelineException
    {}

    public Valve getNextValve(Valve valve)
    	throws PipelineException
    {
        return null;
    }
}
