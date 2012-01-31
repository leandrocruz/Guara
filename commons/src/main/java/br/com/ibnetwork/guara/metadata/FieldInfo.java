package br.com.ibnetwork.guara.metadata;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.commons.lang.StringUtils;

import br.com.ibnetwork.guara.annotation.ColumnInfo;
import br.com.ibnetwork.guara.annotation.Event;
import br.com.ibnetwork.guara.annotation.InputInfo;
import br.com.ibnetwork.guara.forms.Option;
import br.com.ibnetwork.guara.forms.ReferenceCache;
import br.com.ibnetwork.guara.forms.ReferenceLoader;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.JdbcUtils;
import br.com.ibnetwork.xingu.utils.annotation.AnnotationHelper;
import br.com.ibnetwork.xingu.validator.ann.ValidateRequired;

public class FieldInfo 
	implements Comparable<FieldInfo>
{
	private PropertyDescriptor descriptor;

	private BeanInfo beanInfo;

	@SuppressWarnings("unchecked")
    private static final List<Event> emptyList = UnmodifiableList.decorate(new ArrayList<Event>());
	
	@Inject
	private Factory factory;

	public FieldInfo(BeanInfo beanInfo, PropertyDescriptor descriptor)
    {
	    this.descriptor = descriptor;
	    this.beanInfo = beanInfo;
    }

	private Object getBean()
    {
	    return beanInfo.getBean();
    }

	public String getGetter()
	{
		Method readMethod = descriptor.getReadMethod();
		if (readMethod == null)
		{
			return "";
		}
		return readMethod.getName();
	}
	
	public String getSetter()
	{
		Method writeMethod = descriptor.getWriteMethod();
		if (writeMethod == null)
		{
			return "";
		}
		return writeMethod.getName();
	}
	
	public String getName()
	{
		return descriptor.getName();
	}
	
	public String getType()
	{
		return descriptor.getPropertyType().getName();
	}

	public String getTypeName()
	{
		return descriptor.getPropertyType().getSimpleName();
	}

	public String getPackageName()
	{
		Package pack = descriptor.getPropertyType().getPackage();
		return pack == null ? "" : pack.getName();
	}

	
	public String toString()
	{
		return getName()+":"+descriptor.getPropertyType().getName();
	}

	public int compareTo(FieldInfo other) 
	{
		int groupOrder = getGroupOrder();
		int order = groupOrder * 100 + (getOrder() < 0 ? 99 : getOrder());
		int otherGroupOrder = other.getGroupOrder();
		int otherOrder = otherGroupOrder * 100 + (other.getOrder() < 0 ? 99 : other.getOrder());
		
		//System.out.println(getName()+"("+order+") "+other.getName()+"("+otherOrder+") : "+(order - otherOrder));
		return order - otherOrder;
	}
	
	public int getOrder()
    {
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.order();
		}
	    return -1;
    }

	public int getGroupOrder()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.groupOrder();
		}
	    return -1;
	}

	public String getGroupName()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.group();
		}
	    return "";
	}

	/*
	 * Database related
	 */
	private ColumnInfo getColumnInfo()
    {
	    ColumnInfo ann = (ColumnInfo) AnnotationHelper.getFieldAnnotationByType(getBean().getClass(), getName(), ColumnInfo.class);
	    return ann;
    }

	public boolean ignoreColumn()
	{
		ColumnInfo ann = getColumnInfo();
		if(ann != null)
		{
			return ann.ignore();
		}
		return false;
	}
	
	public String getJdbcType()
	{
		ColumnInfo ann = getColumnInfo();
		if(ann != null && ann.jdbcType() != ColumnInfo.Type.UNDEF)
		{
			return ann.jdbcType().name();
		}
		String name = descriptor.getPropertyType().getName();
		String type = JdbcUtils.conversions.get(name);
		if(type == null)
		{
			return "OTHER";
		}
		return type;
	}
	
	public String getColumnName()
	{
		ColumnInfo ann = getColumnInfo();
		if(ann != null)
		{
			String name = ann.name(); 
			if(!StringUtils.isEmpty(name))
			{
				return name;
			}
		}
		return JdbcUtils.toColumnName(getName());
	}

	public boolean isNullable()
    {
		ColumnInfo ann = getColumnInfo();
		if(ann != null)
		{
			return ann.nullable();
		}
    	return true;
    }

	public String getNullValue()
	{
		String type = getJdbcType();
		String nullValue = JdbcUtils.nullValue.get(type);
		return "NULL".equals(nullValue) ? null : nullValue;
	}
	
	public boolean isPrimaryKey()
    {
		ColumnInfo ann = getColumnInfo();
		if(ann != null)
		{
			return ann.primaryKey();
		}
    	return false;
    }
	
	public String getSampleValue()
	{
		String type = getJdbcType();
		return JdbcUtils.samples.get(type);
	}
	
	/*
	 * Form related
	 */

	private InputInfo getFieldInfo()
    {
	    InputInfo ann = (InputInfo) AnnotationHelper.getFieldAnnotationByType(getBean().getClass(), getName(), InputInfo.class);
	    return ann;
    }

	public boolean ignoreField()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return !ann.includeOnDetail() && !ann.includeOnListing();
		}
		return false;
	}

	public boolean includeOnListing()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.includeOnListing();
		}
		return true;
	}

	public boolean includeOnDetail()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.includeOnDetail();
		}
		return true;
	}
	
	public String getInputType()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.inputType();
		}
		return "text";
	}
	
	public String getFieldLabel()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.label();
		}
		return StringUtils.capitalize(getName());
	}
	
	public String getFormat()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			String format = ann.format(); 
			return StringUtils.trimToNull(format);
		}
		return null;
	}

	/* used my iMask */
	public String getMask()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			String format = ann.mask(); 
			return StringUtils.trimToNull(format);
		}
		return null;
	}
	
	public String getRfc3339()
	{
		String format = getFormat();
		if(format == null)
		{
			return null;
		}
		return format;
	}

	public String getReferenceLoader()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.referenceLoader().getName();
		}
		return null;
	}
	
	public String getSize()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.size();
		}
		return null;
	}
	
	public String getMaxLength()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			String maxLength = StringUtils.trimToNull(ann.maxLength()); 
			if(maxLength != null)
			{
				return maxLength;	
			}
		}
		return getSize();
	}

	public String getRows()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.rows();
		}
		return null;
	}

	public String getCols()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			return ann.cols();
		}
		return null;
	}
	
	public String getWidgetType()
	{
		InputInfo ann = getFieldInfo();
		if(ann != null)
		{
			String type = ann.widgetType(); 
			return StringUtils.trimToNull(type);
		}
		return null;
	}

	public Object getValue() 
		throws Exception
    {
		Method readMethod = descriptor.getReadMethod();
	    if(readMethod == null)
	    {
	    	return null;
	    }
	    Object result = readMethod.invoke(getBean(),(Object[])null);
	    return result;
    }
	
	public String getValueFormatted()
		throws Exception
	{
		return getValueFormatted(beanInfo.getLocale());
	}
	
	/*
	 * Used on view (freemarker) templates
	 */
	public String getValueFormatted(Locale locale)
		throws Exception
	{
		Object value = getValue();
		if(value == null)
		{
			return "";
		}
		String format = getFormat();
		String inputType = getInputType();
		String valueAsString = ConvertUtils.convert(value); 
		if("select".equalsIgnoreCase(inputType))
		{
			String loaderClass = getReferenceLoader();
			ReferenceLoader loader = ReferenceCache.getLoader(factory, loaderClass);
			Option op = loader.loadOption(valueAsString);
			if(op != null)
			{
				return op.getValue();	
			}
		}
		if(format == null)
		{
			return valueAsString;
		}
		else
		{
			return tryFormatter(locale);
		}
	}

	private String tryFormatter(Locale locale)
		throws Exception
    {
		Class<?> clazz = descriptor.getPropertyType();
		Object value = getValue();
		String format = getFormat();
		Format formatter = null;
		if(Date.class.equals(clazz))
		{
			formatter = new SimpleDateFormat(format,locale);
			return formatter.format(value);
		}
		else if(clazz.isPrimitive())
		{
			formatter = new MessageFormat(format,locale);
			return formatter.format(new Object[]{value});
		}
		if(value instanceof String)
		{
			return (String) value;
		}
		return null;
    }

	public boolean isRequired()
	{
		ValidateRequired ann = (ValidateRequired) AnnotationHelper.getFieldAnnotationByType(getBean().getClass(), getName(), ValidateRequired.class);
	    if(ann != null)
	    {
	    	return ann.required();
	    }
	    return false;
	}

	public List<Event> getEvents()
	{
		Event ann = (Event) AnnotationHelper.getFieldAnnotationByType(getBean().getClass(), getName(), Event.class);
		if(ann == null)
		{
			return emptyList;
		}
		List<Event> result = new ArrayList<Event>();
		result.add(ann);
		return result;
	}
	
	public boolean isCollection()
	{
		return Collection.class.isAssignableFrom(descriptor.getPropertyType());
	}

	public boolean isFrozen() 
		throws Exception
	{
		InputInfo ann = getFieldInfo();
		FieldInfo id = beanInfo.getId();
		if(ann == null || id == null)
		{
			return false;
		}
		Number number = (Number) id.getValue();
		return number.longValue() > 0 && ann.freeze(); 
	}

}