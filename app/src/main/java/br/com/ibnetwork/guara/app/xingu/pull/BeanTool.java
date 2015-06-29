package br.com.ibnetwork.guara.app.xingu.pull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.guara.metadata.BeanInfo;
import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;
import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.store.ObjectStore;
import xingu.utils.ObjectUtils;
import xingu.validator.BeanValidator;
import xingu.validator.ValidatorContext;

public class BeanTool
    extends ApplicationToolSupport
    implements Configurable
{
	@Inject
	protected ObjectStore store;
	
	@Inject
	protected Factory factory;
	
	@Inject
	protected BeanValidator validator;
	
	private List<String> packageNames;
	
	private Map<String, BeanInfo> metaCache = new HashMap<String, BeanInfo>(20);;
	
	private Map<String, Class> classCache = new HashMap<String, Class>(20);

	public void configure(Configuration configuration)
    	throws ConfigurationException
    {
		Configuration[] conf = configuration.getChild("packages").getChildren("package");
		packageNames = new ArrayList<String>(conf.length);
		for (int i = 0; i < conf.length; i++)
        {
			String packageName = conf[i].getAttribute("name");
			packageNames.add(packageName);
        }
    }

	public Object getById(String className, long id)
		throws ClassNotFoundException
	{
		Class clazz = getBeanClass(className);
		return store.getById(clazz, id);
	}

	public List getAll(String className)
		throws ClassNotFoundException
	{
		Class clazz = getBeanClass(className);
		return store.getAll(clazz);
	}
	
	public BeanInfo getBeanInfo(Object bean)
	{
		BeanInfo info = (BeanInfo) factory.create(BeanInfo.class, new Object[]{bean}, new String[]{Object.class.getName()});
		return info;
	}

	public BeanInfo getBeanInfo(String className)
		throws ClassNotFoundException
	{
		BeanInfo metadata = metaCache.get(className);
		if(metadata == null)
		{
			Class clazz = getBeanClass(className);
			metadata = (BeanInfo) factory.create(BeanInfo.class, new Object[]{clazz});
			metaCache.put(className, metadata);
		}
		return metadata;
	}
	
	public ValidatorContext validate(Object bean) 
		throws Exception
	{
    	ValidatorContext ctx = new ValidatorContext();
    	validator.validate(bean, ctx);
    	return ctx;
	}

	private Class getBeanClass(String className) 
		throws ClassNotFoundException
	{
		Class clazz = classCache.get(className);
		if(clazz != null)
		{
			return clazz;
		}
		for (String packageName : packageNames)
        {
			try
			{
				clazz = ObjectUtils.loadClass(packageName+"."+className);
			}
			catch(Throwable t)
			{
				//ignored
			}
			if(clazz != null)
			{
				classCache.put(className, clazz);
				return clazz;
			}
        }
		throw new ClassNotFoundException("Can't find class: "+className+" in configured packages: "+packageNames);
	}
}