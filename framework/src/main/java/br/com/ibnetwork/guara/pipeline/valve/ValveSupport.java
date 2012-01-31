package br.com.ibnetwork.guara.pipeline.valve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.Valve;
import br.com.ibnetwork.guara.rundata.RunData;

public abstract class ValveSupport
	implements Valve
{
    protected String name;

    protected Pipeline pipeline;
    
    protected Logger log = LoggerFactory.getLogger(getClass());

    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public Pipeline getPipeline()
    {
        return pipeline;
    }
    
    public void setPipeline(Pipeline pipeline)
    {
        this.pipeline = pipeline;
    }

    /**
     * Execute next valve in pipeline
     */
    private void keepGoing(RunData data)
    {
        Valve next = pipeline.getNextValve(this);
        if(next != null)
        {
        	next.forward(data);    
        }
    }

    public final void forward(RunData data)
		throws PipelineException
	{
   		boolean result = execute(data);
   		if(result == true)
        {
   		    keepGoing(data);    
        }
	}

	protected abstract boolean execute(RunData data)
		throws PipelineException;

}
