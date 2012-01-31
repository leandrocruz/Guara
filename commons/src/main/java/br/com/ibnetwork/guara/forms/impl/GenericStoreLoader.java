package br.com.ibnetwork.guara.forms.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.forms.Option;
import br.com.ibnetwork.xingu.container.Inject;
import xingu.store.ObjectStore;
import xingu.store.PersistentBean;

public abstract class GenericStoreLoader<POJO extends PersistentBean>
	extends ReferenceLoaderSupport
{
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	protected ObjectStore store;
	
	private Option[] options;
	
	private String valueField;
	
	private String displayField;
	
	private Class<POJO> pojoClass;
		
	public GenericStoreLoader(Class<POJO> pojoClass, String id, String display) 
	{
		this.pojoClass = pojoClass;
		this.valueField = id;
		this.displayField = display;
	}
	
	@Override
    protected Option[] getOptions()
		throws Exception
	{
		if(useCache() && options != null)
		{
			log.info("Returning options from cache: "+options.length);
			return options;
		}
		
		List<POJO> all = store.getAll(pojoClass);
		if(all == null || all.size() == 0)
		{
			options = new Option[]{}; 
			return options;
		}
		
		Option[] result = toArray(all);
		if(useCache())
		{
			options = result;
		}
		return result;
    }

	protected Option[] toArray(List<POJO> list)
		throws Exception
	{
		Option[] result = new Option[list.size()];
		int i=0;
		for (POJO pojo : list)
        {
	        result[i++] = new OptionImpl(getId(pojo), getDesc(pojo)); 
        }
		return result;
		
	}
	
	protected boolean useCache()
    {
	    return false;
    }

	protected String getId(POJO pojo) 
		throws Exception 
	{
		return callGetter(pojo, valueField);
	}

	protected String getDesc(POJO pojo) 
		throws Exception 
	{
		return callGetter(pojo, displayField);
	}
	
	private String callGetter(Object pojo, String method)
		throws Exception
	{
		PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(pojo, method);
		Method m = PropertyUtils.getReadMethod(descriptor);
		Object result = m.invoke(pojo, (Object[])null);
		return result.toString();
	}
}