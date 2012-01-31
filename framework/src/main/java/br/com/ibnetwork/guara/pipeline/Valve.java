package br.com.ibnetwork.guara.pipeline;

import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public interface Valve
{
    String getName();
    
    void setName(String name);
    
    Pipeline getPipeline();
    
    void setPipeline(Pipeline pipeline);

    void forward(RunData data)
    	throws PipelineException;
}
