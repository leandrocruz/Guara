package br.com.ibnetwork.guara.modules;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public interface ModuleLoader
{
    String ROLE = ModuleLoader.class.getName();
    
    String SCREEN_TYPE = "screens";
    
    String ACTION_TYPE = "actions";
    
    Module loadModule(String name, String type)
    	throws ModuleLoaderException;

    Class loadModuleClass(String name, String type)
		throws ModuleLoaderException;

}
