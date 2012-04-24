package br.com.ibnetwork.guara.pipeline.valve.modules;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.injector.Injector;
import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleLoader;
import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.xingu.container.Inject;
import xingu.template.Context;

public abstract class ModuleExecutorSupport
    extends ValveSupport
    implements Configurable
{
    protected Log logger = LogFactory.getLog(ModuleExecutorSupport.class);
    
    @Inject
    protected ModuleLoader loader;
    
    @Inject
    private Injector injector;
    
    protected boolean recurse;

    public void configure(Configuration conf)
    	throws ConfigurationException
    {
        recurse = true;
    }

    
    public boolean execute(RunData runData) 
    	throws PipelineException
    {
        String name = getModuleName(runData);
        String type = getModuleType(runData);
        if(name != null)
        {
            if(logger.isDebugEnabled()) logger.debug("Executing module ["+name+"] type["+type+"]");
            try
            {
                Module module = loader.loadModule(name,type);
                if(module.isThreadSafe())
                {
                	injector.inject(module, runData.getParameters());
                }
                Outcome outcome = executeModule(module,runData);
                populateContext(module, runData.getContext());
                if(outcome != null && !Outcome.UNKNOWN.equals(outcome)) 
                {
                	runData.setOutcome(outcome);
                }
            }
            catch (Exception e)
            {
                throw new PipelineException("Error executing module["+name+"] type["+type+"]", e);
            }
        }
        return true;
    }



	private void populateContext(Module module, Context ctx) 
    	throws Exception
    {
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(module);
		for (int i = 0; i < descriptors.length; i++)
        {
	        PropertyDescriptor descriptor = descriptors[i];
	        String name = descriptor.getName();
	        Method reader =  descriptor.getReadMethod();
	        if(reader != null)
	        {
		        Object value = reader.invoke(module, (Object[]) null);
		        ctx.put(name, value);	
	        }
        }
    }


	protected abstract String getModuleName(RunData runData);

    protected abstract String getModuleType(RunData runData);
    
    protected Outcome executeModule(Module module, RunData runData) 
    	throws Exception
    {
        Outcome outcome = module.doPerform(runData, runData.getContext());
        return outcome;
    }

}
