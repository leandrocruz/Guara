package br.com.ibnetwork.guara.metadata;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import br.com.ibnetwork.guara.bean.BeanUtils;
import xingu.store.PersistentBean;
import br.com.ibnetwork.xingu.utils.FieldUtils;

public class BeanInfo
{
	private Object _bean;
	
	private String _beanName;
	
	private Locale _locale = Locale.US;
	
	public BeanInfo(Object bean)
	{
		_bean = bean;
		_beanName = BeanUtils.getBeanName(_bean);
	}

	public Object getBean() 
	{
		return _bean;
	}

	public String getBeanName()
    {
    	return _beanName;
    }

	public Locale getLocale()
    {
	    return _locale;
    }
	
	public boolean hasPrimaryKey()
	{
		if(_bean instanceof PersistentBean)
		{
			PersistentBean pb = (PersistentBean) _bean;
			return pb.getId() > 0;
		}
		return false;
	}
	
	public List<FieldInfo> getProperties()
	{
		return getProperties(true);
	}
	
	public List<FieldInfo> getProperties(boolean sort)
    {
		Class<?> beanClass = _bean.getClass();
		List<FieldInfo> result = new ArrayList<FieldInfo>();
		List<Field> fields = FieldUtils.getAllFields(beanClass);
		for (Field field : fields)
        {
			String name = field.getName();
			PropertyDescriptor descriptor = getDescriptor(beanClass, name);
			if(descriptor != null && !"class".equals(name) && !"display".equals(name))
			{
		        FieldInfo fieldInfo = new FieldInfo(this, descriptor); 
		        result.add(fieldInfo);
			}
        }
		if(sort)
		{
			Collections.sort(result);	
		}
		return result;
	}

	
	private PropertyDescriptor getDescriptor(Class<?> beanClass, String name)
    {
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(beanClass);
		for (PropertyDescriptor descriptor : properties)
        {
			if(name.equals(descriptor.getName()))
			{
				return descriptor;
			}
        }
		return null;
    }

	public List<FieldInfo> getInputFields()
		throws Exception
	{
		return getInputFields(true);
	}
	
	public List<FieldInfo> getInputFields(boolean sort) 
		throws Exception
	{
		List<FieldInfo> properties = getProperties(sort);
		List<FieldInfo> result = new ArrayList<FieldInfo>(properties.size());
		for (FieldInfo info : properties)
        {
			//System.out.println("InputField: "+info.getName()+" ignored? "+info.ignoreField());
	        if(!info.ignoreField())
	        {
	        	result.add(info);
	        }
        }
		return result;
	}
	
	public List<FieldInfo> getColumns() 
		throws Exception
	{
		List<FieldInfo> properties = getProperties();
		List<FieldInfo> result = new ArrayList<FieldInfo>(properties.size());
		for (FieldInfo info : properties)
		{
			//System.out.println("Column: "+info.getName()+" ignored? "+info.ignoreColumn());
			if(!info.ignoreColumn())
			{
				result.add(info);
			}
		}
		return result;
	}
	
	public Set<FieldInfo> getImports() 
		throws Exception
	{
		List<FieldInfo> properties = getProperties();
		Set<FieldInfo> result = new HashSet<FieldInfo>();
		for (FieldInfo info : properties)
		{	
			if(!info.ignoreColumn())
			{
				if (!info.getPackageName().equals("") &&
					!info.getPackageName().equals("java.lang"))
				{
					result.add(info);	
				}
			}
		}
		return result;
	}	
	
	/*
	 * comma separated
	 */
	public String joinColumnNames(String replacement)
		throws Exception
	{
		List<FieldInfo> columns = getColumns();
		List<String> list = new ArrayList<String>();
		for (FieldInfo info : columns)
        {
			String columnName = info.getColumnName();
			//System.out.println("BeanInfo.joinColumnNames() Coluna: "+columnName);
			String value = StringUtils.isNotEmpty(replacement) ? replacement : columnName;
			list.add(value);
        }
		String result = StringUtils.join(list.iterator(), ",");
		return result;
	}

	public FieldInfo getId()
    {
		try
		{
			FieldInfo id = getInputField("id");
			return id;
		}
		catch(Exception e)
		{
			//ignored
		}
		return null;
    }

	public FieldInfo getInputField(String fieldName) 
		throws Exception
    {
		List<FieldInfo> fields = getInputFields();
		for (FieldInfo field : fields)
        {
	        if(field.getName().equals(fieldName))
	        {
	        	return field;
	        }
        }
		return null;
		//throw new Exception("Can't find field: "+fieldName);
    }
	
	public String getValueFormatted(String fieldName)
		throws Exception
	{
		FieldInfo field = getInputField(fieldName);
		if(field == null)
		{
		    return null;
			//throw new Exception("Can't find field: "+fieldName);
		}
		return field.getValueFormatted();
	}
	
	/*
	 * See: http://www.w3.org/TR/html4/interact/forms.html#adef-enctype
	 */
	public String getFormEncType() 
		throws Exception
	{
		
		List<FieldInfo> fields = getFileItems();
		if(fields.size() > 0)
		{
			return "multipart/form-data";
		}
		return "application/x-www-form-urlencoded";
	}

	public List<FieldInfo> getFileItems() 
		throws Exception
	{
		List<FieldInfo> fields = getInputFields();
		List<FieldInfo> result = new ArrayList<FieldInfo>();
		for (FieldInfo info : fields)
        {
	        if("file".equals(info.getInputType()))
	        {
	        	result.add(info);
	        }
        }
		return result;
	}

    public String getFieldPrefix()
    {
        return getBeanName() + ".";
    }

    @Override
    public String toString()
    {
        return "BeanInfo for: "+_bean;
    }
}
