package br.com.ibnetwork.guara.bean;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

public class BeanUtils
{
	private static final Pattern patternIndex = Pattern.compile("(.+)\\[(\\d+)\\]\\.(.+)");
	
	private static final Pattern patternNoIndex = Pattern.compile("(.+)\\.(.+)");
	
	public static String getBeanName(Object bean)
	{
		Class<?> beanClass = bean.getClass();
		try
		{
		    Method method = beanClass.getMethod("getBeanName", (Class<?>[]) null);
		    return (String) method.invoke(bean, (Object[]) null);
		}
		catch (Throwable t)
		{
		    //ignored
		}
		return getBeanName(beanClass);
	}
	
	public static String getBeanName(Class<?> beanClass)
	{
		String name = ClassUtils.getShortClassName(beanClass); 
		return StringUtils.uncapitalize(name);
	}

	/**
	 * Splits a property into: object name, index and property name
	 */
	public static Property split(String fieldName)
	{
		Matcher m = patternIndex.matcher(fieldName); 
		if(m.matches())
		{
			Property p = new Property();
			p.objectName = m.group(1);
			p.index = Integer.parseInt(m.group(2));
			p.propertyName = m.group(3);
			return p;
		}
		else
		{
			m = patternNoIndex.matcher(fieldName); 
			if(m.matches())
			{
				Property p = new Property();
				p.objectName = m.group(1);
				p.index = -1;
				p.propertyName = m.group(2);
				return p;
			}
		}
		return null;
	}
}
