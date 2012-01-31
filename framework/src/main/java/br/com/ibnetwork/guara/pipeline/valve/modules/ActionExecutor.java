package br.com.ibnetwork.guara.pipeline.valve.modules;


import java.util.Iterator;
import java.util.Set;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.commons.beanutils.MethodUtils;

import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleLoader;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author leandro
 */
public class ActionExecutor
    extends ModuleExecutorSupport
    implements Configurable
{
    public static final String METHOD_KEY = "exec_";
    
    protected String getModuleName(RunData runData)
    {
        return runData.getPageInfo().getActionName();
    }
    
    protected String getModuleType(RunData runData)
    {
        return ModuleLoader.ACTION_TYPE;
    }

    protected Outcome executeModule(Module module, RunData runData) 
		throws Exception
	{
        String methodName = getMethodName(runData);
        Outcome result = null;
        if(methodName != null)
        {
            if(logger.isDebugEnabled()) logger.debug("Executing method ["+methodName+"]");
            Object obj = MethodUtils.invokeMethod(module,methodName,new Object[]{runData, runData.getContext()});
            if(obj instanceof Outcome)
            {
                result = (Outcome) obj;    
            }
        }
        else
        {
            if(logger.isDebugEnabled()) logger.debug("Executing doPerform()");
            result = module.doPerform(runData, runData.getContext());    
        }
        return result;
	}

  private String getMethodName(RunData runData)
  {
      Set keys = runData.getParameters().keySet();
      for (Iterator iter = keys.iterator(); iter.hasNext();)
      {
          String key = (String) iter.next();
          if(key.startsWith(METHOD_KEY))
          {
              String methodName = key.substring(METHOD_KEY.length());
              return methodName;
          }
      }
      return null;
  }
}
