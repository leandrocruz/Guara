package br.com.ibnetwork.guara.app.modules.actions;

import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.guara.app.modules.Result;
import br.com.ibnetwork.guara.metadata.BeanInfo;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import xingu.store.PersistentBean;
import br.com.ibnetwork.xingu.template.Context;
import xingu.validator.ValidatorContext;

public abstract class SingleCrudControlSupport
    extends CrudControlSupport
{
	public Outcome store(RunData data, Context ctx) 
		throws Exception
    {
		PersistentBean bean = createFromRequest(data);

    	List<Result> results = new ArrayList<Result>(1);
		ctx.put("storeResults", results);
		ValidatorContext vCtx = runValidator(data, bean);
		Result result = createResult(bean, vCtx); 
		results.add(result);
		
    	if(vCtx.getErrorCount() > 0)
    	{
    		data.setMessage(getSystemMessage("formError"));
    		return Outcome.error(this, "store");
    	}
    	try
    	{
    		storeObject(data, bean);
    		data.setMessage(getSystemMessage("stored"));
    		return Outcome.success(this,"store");
    	}
    	catch(Throwable t)
    	{
    		log.error("Error storing bean", t);
    		data.setMessage(getSystemMessage("storeError"));
    		return Outcome.error(this,"store");
    	}
    }

	public Outcome delete(RunData data, Context ctx) 
		throws Exception
    {
    	long id = data.getParameters().getLong("id");
    	PersistentBean bean = doGet(id);
    	Object[] msgParams = new Object[]{bean.toString()};
    	try
    	{
    		beforeDelete(data,bean);
    		doDelete(bean);
    		afterDelete(data,bean);
    		data.setMessage(getSystemMessage("deleted"),msgParams);
    		return Outcome.success(this,"delete");
    	}
    	catch(Throwable t)
    	{
    		log.error("Error deleting bean", t);
    		data.setMessage(getSystemMessage("deleteError"),msgParams);
    		return Outcome.error(this,"delete");
    	}
    } 
	
    protected PersistentBean doGet(long id)
    	throws Exception
    {
    	return store.getById(getBeanClass(), id);
    }

	protected BeanInfo getBeanInfo(Object bean)
	{
		if(bean == null)
		{
			return getBeanInfo();
		}
		else
		{
			return (BeanInfo) factory.create(BeanInfo.class, new Object[]{bean}, new String[]{Object.class.getName()});
		}
	}

	protected BeanInfo getBeanInfo()
	{
		Class clazz = getBeanClass();
		return (BeanInfo) factory.create(BeanInfo.class, new Object[]{clazz});
	}

	protected abstract Class getBeanClass();
	
	protected abstract PersistentBean createFromRequest(RunData data)
		throws Exception;

}
