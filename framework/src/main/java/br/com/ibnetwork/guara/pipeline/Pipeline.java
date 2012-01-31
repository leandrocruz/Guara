package br.com.ibnetwork.guara.pipeline;

import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public interface Pipeline
{
    String DEFAULT_PIPELINE_NAME = "default";
    
    String getName();

    void execute(RunData runData)
    	throws PipelineException;

    Valve[] getValves();
    
    /**
     * Null if not found
     * 
     * @param valveName
     * @return
     */
    Valve getValveByName(String valveName);
    
    void addValve(Valve valve)
    	throws PipelineException;
    
    void removeValve(Valve valve)
    	throws PipelineException;

    /**
     * Null if last or not found
     * 
     * @param valve
     * @return
     * @throws PipelineException
     */
    Valve getNextValve(Valve valve)
    	throws PipelineException;

}
