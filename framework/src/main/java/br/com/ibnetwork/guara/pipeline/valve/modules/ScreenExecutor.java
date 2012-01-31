package br.com.ibnetwork.guara.pipeline.valve.modules;


import org.apache.avalon.framework.configuration.Configurable;

import br.com.ibnetwork.guara.modules.ModuleLoader;
import br.com.ibnetwork.guara.rundata.RunData;

/**
 * @author leandro
 */
public class ScreenExecutor
    extends ModuleExecutorSupport
    implements Configurable
{

    protected String getModuleName(RunData runData)
    {
        String screen = runData.getPageInfo().getScreenName();
        String template = runData.getPageInfo().getTemplate();
        return screen != null ? screen : template;
    }
    
    protected String getModuleType(RunData runData)
    {
        return ModuleLoader.SCREEN_TYPE;
    }


}
