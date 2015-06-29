package br.com.ibnetwork.guara.injector.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.injector.Injector;
import br.com.ibnetwork.guara.parameters.Parameters;
import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.utils.convert.DateConverter;

public class InjectorImpl
	implements Injector, Configurable
{
	private String SEPARATOR = "::";
	
	private Log log = LogFactory.getLog(InjectorImpl.class);
	
	@Inject
	private Factory factory; 
	
	public void configure(Configuration conf)
    	throws ConfigurationException
    {
		conf = conf.getChild("format");
		String separator = conf.getAttribute("separator",SEPARATOR);
		String language = conf.getAttribute("locale-language","pt");
		String country = conf.getAttribute("locale-country","BR");
		Locale locale = new Locale(language, country);
		ConvertUtils.register(new DateConverter(locale, separator), Date.class);
    }
	
	public void inject(Object module, Parameters params)
		throws Exception
    {
    	Map<String, String> values = parseValues(params.getValueMap());
    	PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(module);
    	for (int i = 0; i < descriptors.length; i++)
        {
	        PropertyDescriptor descriptor = descriptors[i];
	        Class beanClass = descriptor.getPropertyType();
	        String name = descriptor.getName();
	        if(!ignoreClass(beanClass) && foundValues(name, values))
	        {
	        	log.info("Creating bean: "+beanClass.getName()+" on property: "+name);	
	        	Object bean;
	        	if(List.class.equals(beanClass))
	        	{
	        		Class type = getGenericReturnType(descriptor);
	        		bean = LazyList.decorate(new ArrayList(), new BeanFactory(factory,type));
	        	}
	        	else
	        	{
	        		bean = factory.create(beanClass);
	        	}
		        PropertyUtils.setProperty(module, name, bean);
	        }
        }
    	if(log.isDebugEnabled())
    	{
    		log.debug("Injecting values: "+values+" on: "+module.getClass().getName());
    	}
    	populateBean(module, values);
    }

	private Class getGenericReturnType(PropertyDescriptor descriptor)
    {
		Method method = descriptor.getReadMethod();
	    Type returnType = method.getGenericReturnType(); 

	    if (returnType instanceof ParameterizedType) 
	    {
	       ParameterizedType type = (ParameterizedType) returnType;
	       Type[] typeArguments = type. getActualTypeArguments() ;
	       for (Type t : typeArguments)
	       {
	    	   return (Class) t;
	       }
	    }
	    return null;
    }

	private void populateBean(Object obj, Map<String, String> values)
        throws IllegalAccessException, InvocationTargetException
    {
	    BeanUtils.populate(obj, values);
    }

	private boolean foundValues(String name, Map<String, String> values)
    {
		String simple = name+".";
		String indexed = name+"[";
		for (Iterator<String> iter = values.keySet().iterator(); iter.hasNext();)
        {
	        String key = iter.next();
	        if(key.startsWith(simple) || key.startsWith(indexed))
	        {
	        	return true;
	        }
        }
	    return false;
    }

	private Map<String, String> parseValues(Map<String, String[]> map)
    {
	    Map<String, String> values = new HashMap<String, String>();
    	for (Iterator iter = map.keySet().iterator(); iter.hasNext();)
    	{
    		String key = (String) iter.next();
    		String[] value = map.get(key);
    		if(value != null && value.length > 0)
    		{
    			String valueAsString = value[0];
    			String mask = null;
        		if(key.indexOf(SEPARATOR) > -1)
        		{
        			String[] array = key.split(SEPARATOR); 
        			key = array[0];
        			mask = array[1];
        		}
        		valueAsString = mask != null ? mask+SEPARATOR+valueAsString : valueAsString;
        		values.put(key, valueAsString);
    		}
    	}
	    return values;
    }

	private static boolean ignoreClass(Class clazz)
    {
		return clazz.isPrimitive()
		|| clazz.getName().startsWith("java.lang")
		|| clazz.getName().startsWith("javax");
    }
}

class BeanFactory
	implements org.apache.commons.collections.Factory
{
	private Class beanClass;
	
	private Factory factory; 
	
	public BeanFactory(Factory factory, Class beanClass)
	{
		this.beanClass = beanClass;
		this.factory = factory;
	}
	
	public Object create()
    {
	    return factory.create(beanClass);
    }
}