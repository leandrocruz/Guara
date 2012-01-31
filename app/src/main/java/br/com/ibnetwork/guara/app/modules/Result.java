package br.com.ibnetwork.guara.app.modules;

import br.com.ibnetwork.guara.bean.BeanUtils;
import br.com.ibnetwork.guara.metadata.BeanInfo;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import xingu.store.PersistentBean;
import br.com.ibnetwork.xingu.validator.ValidatorContext;

public class Result
{
	@Inject
	private Factory factory;
	
	private PersistentBean bean;
	
	private ValidatorContext ctx;
	
	private BeanInfo beanInfo;
	
	private int index = -1;
	
	private String prefix;
	
	public PersistentBean getBean()
    {
    	return bean;
    }

	public void setBean(PersistentBean bean)
	{
		this.bean = bean;
    }

	public ValidatorContext getValidatorContext()
    {
    	return ctx;
    }
	
	public void setValidatorContext(ValidatorContext ctx)
	{
		this.ctx = ctx;
	}
	
	public String getBeanName()
		throws Exception
	{
		return BeanUtils.getBeanName(bean);	
	}
	
	public BeanInfo getBeanInfo()
	{
		if(beanInfo == null)
		{
			beanInfo = (BeanInfo) factory.create(BeanInfo.class, new Object[]{bean}, new String[]{Object.class.getName()});	
		}
		return beanInfo;
	}

	public int getIndex()
    {
    	return index;
    }

	public void setIndex(int index)
    {
		this.index = index;
    }
	
	public String getPrefix()
		throws Exception
	{
		if(prefix != null)
		{
			return prefix;
		}
		if(bean instanceof Prefixable)
		{
			prefix = ((Prefixable)bean).getPrefix();
		}
		if(prefix == null)
		{
			prefix = getBeanName();
		}
		return prefix;
	}
	
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
}
