package br.com.ibnetwork.guara.modules.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ThreadSafe;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;

public class Loader
{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    private List packages;
    
    private boolean recurse;
    
    @Inject
    private Factory factory;
    
    private Map<String, Module> cache;

    private String defaultModuleName;
    
    public Loader(Boolean recurse, String defaultModuleName)
    {
        this.recurse = recurse;
        this.defaultModuleName = defaultModuleName;
        this.cache = new HashMap<String, Module>();
    }

    public void setPackageList(List packageList)
    {
        packages = packageList;
    }

    public boolean isRecursive()
    {
        return recurse;
    }
    
    public Module load(String name)
    {
        Module module = cache.get(name);
        if(module != null)
        {
            if(logger.isDebugEnabled()) logger.debug("Module ["+name+"] cached");
            return module;
        }
        
        //create module instance
        Class moduleClass = loadClass(name);
        logger.info("Module ["+name+"] found ["+moduleClass.getName()+"]");
        module = (Module) factory.create(moduleClass);
        module.setName(name);
        
        ThreadSafe ann = module.getClass().getAnnotation(ThreadSafe.class);
        if(ann == null)
        {
        	cache.put(name,module);	
        }
        else
        {
        	module.setThreadSafe(true);
        }
        return module;
    }

    public Class loadClass(String name)
    {
        Class moduleClass = serachModuleClass(name);
        if(moduleClass == null && recurse)
        {
            moduleClass = recurse(name);
        }
        return moduleClass;
    }

    private Class serachModuleClass(String name)
    {
        if(logger.isDebugEnabled()) logger.debug("searching for module ["+name+"]");
        for (Iterator iter = packages.iterator(); iter.hasNext();)
        {
            String packageName = (String) iter.next();
            String className = packageName + name;
            if(logger.isDebugEnabled()) logger.debug("className is ["+className+"]");
            try
            {
            	Class clazz = Class.forName(className);
            	return clazz;
            }
            catch(ClassNotFoundException e)
            {
                //ignore. move to next package
            }
        }
        return null;

    }
    
    private Class recurse(String name)
    {
        String[] packageNamesInModule = StringUtils.split(name,".");
        if(packageNamesInModule.length == 1)
        {
            //no subpackages
            Class moduleClass = serachModuleClass(defaultModuleName);
            return moduleClass;
        }
        else
        {
            int slots = packageNamesInModule.length;
            String discarded = "";
            for(int i = slots-1; i >= 0; i--)
            {
                discarded = "." + packageNamesInModule[i] + discarded;
                int index = name.indexOf(discarded);
                if(index >= 0)
                {
                    String newName = name.substring(0,index) + "." + defaultModuleName;
                    Class moduleClass = serachModuleClass(newName);
                    if(moduleClass != null)
                    {
                        return moduleClass;
                    }
                }
                else
                {
                    return serachModuleClass(defaultModuleName);   
                }
            }
        }
        return null;
    }
}
