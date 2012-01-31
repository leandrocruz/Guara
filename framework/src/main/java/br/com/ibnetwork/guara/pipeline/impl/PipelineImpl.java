package br.com.ibnetwork.guara.pipeline.impl;

import org.apache.commons.lang.ArrayUtils;

import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.Valve;
import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class PipelineImpl 
	implements Pipeline
{
    private Valve[] valves;
    
    private String name;
    
    public PipelineImpl(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void execute(RunData runData)
    {
    	Valve start = valves[0];
       	start.forward(runData);
    }

    public Valve[] getValves()
    {
        return valves;
    }

    public Valve getValveByName(String valveName)
    {
        for (int i = 0; i < valves.length; i++)
        {
            Valve valve = valves[i];
            if(valve.getName().equals(valveName))
            {
                return valve;
            }
        }
        return null;
    }

    //TODO: synchonized this to enabled dynamic pipeline changes
    public void addValve(Valve valve)
            throws PipelineException
    {
        if(valves == null)
        {
            valves = new Valve[0];
        }
        int size = valves.length;
        Valve[] tmp = new Valve[size + 1];
        tmp[size] = valve;
        System.arraycopy(valves,0,tmp,0,size);
        valves = tmp;
    }
    
    //TODO: synchonized this to enabled dynamic pipeline changes
    public void removeValve(Valve valve)
            throws PipelineException
    {
    }

    public Valve getNextValve(Valve valve)
            throws PipelineException
    {
        int index = ArrayUtils.indexOf(valves,valve);
        return index < 0 || index == valves.length -1 ? null : valves[index+1];
    }

}
