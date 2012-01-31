package br.com.ibnetwork.guara.modules.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleLoader;
import br.com.ibnetwork.guara.modules.ModuleLoaderException;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class ModuleLoaderImpl
	implements ModuleLoader, Configurable
{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Inject
    private Factory factory;
    
    private Map<String, Loader> registry;
    
    public void configure(Configuration conf)
		throws ConfigurationException
	{
        Configuration[] loaders = conf.getChildren("loader");
        registry = new HashMap<String, Loader>(loaders.length);
        for (int i = 0; i < loaders.length; i++)
        {
            Configuration loaderConf = loaders[i];
            String type = loaderConf.getAttribute("type");
            boolean recurse = loaderConf.getAttributeAsBoolean("recurse",true);
            String defaultModuleName = loaderConf.getAttribute("defaultModuleName","Default");
            logger.info("Creating loader for type["+type+"]");
            Loader loader = (Loader) factory.create(Loader.class,new Object[]{recurse,defaultModuleName});
            Configuration[] packages = loaderConf.getChild("packages").getChildren("package");
            List<String> packageList = new ArrayList<String>(packages.length);
            for (int j = 0; j < packages.length; j++)
            {
                String packageName = packages[j].getAttribute("name");
                if(packageName != null)
                {
                    logger.info("package["+packageName+"] added");
                    packageList.add(packageName + ".");
                }
            }
            loader.setPackageList(packageList);
            registry.put(type,loader);
        }
	}

    public Module loadModule(String name, String type)
    	throws ModuleLoaderException
    {
        Loader loader = getLoader(type);
        try
        {
            Module module = loader.load(name);
            return module;    
        }
        catch(Exception e)
        {
            throw new ModuleLoaderException("Error loading module name["+name+"] type["+type+"]",e);
        }
    }

	public Class loadModuleClass(String name, String type) 
		throws ModuleLoaderException
    {
        Loader loader = getLoader(type);
        try
        {
            Class moduleClass = loader.loadClass(name);
            return moduleClass;
        }
        catch(Exception e)
        {
            throw new ModuleLoaderException("Error loading module class, name["+name+"] type["+type+"]",e);
        }
    }

	private Loader getLoader(String type) 
		throws ModuleLoaderException
	{
		Loader loader = registry.get(type);
		if(loader == null)
		{
			throw new ModuleLoaderException("No loader found for type ["+type+"]");
		}
		return loader;
	}
}
