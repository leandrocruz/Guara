package br.com.ibnetwork.guara.app.modules.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.com.ibnetwork.guara.app.modules.Result;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import xingu.store.PersistentBean;
import br.com.ibnetwork.xingu.template.Context;
import br.com.ibnetwork.xingu.validator.ValidatorContext;

public abstract class MultipleCrudControlSupport
    extends CrudControlSupport
{

	public Outcome store(RunData data, Context ctx) 
		throws Exception
	{
		List fromRequest = createFromRequest(data);
		List<PersistentBean> beans = new ArrayList<PersistentBean>(fromRequest.size());
		
		boolean foundErrors = false;
		List<Result> results = new ArrayList<Result>(fromRequest.size());
		ctx.put("storeResults", results);
		
		for (Iterator iter = fromRequest.iterator(); iter.hasNext();)
        {
	        Object obj = (Object) iter.next();
	        if(obj instanceof PersistentBean)
	        {
	        	PersistentBean bean = (PersistentBean) obj;
	        	beans.add(bean);
	        	foundErrors = validateAndCreateResult(data, bean, results, -1) || foundErrors; 
	        }
	        /*
	         * if we have a list, we need to add index information to each result 
	         */
	        else if(obj instanceof Collection)
	        {
	        	Collection coll = (Collection) obj;
	    		int i=0;
	        	for (Iterator iterator = coll.iterator(); iterator.hasNext();)
                {
	                PersistentBean bean = (PersistentBean) iterator.next();
	                if(bean != null)
	                {
		                beans.add(bean);
		                foundErrors = validateAndCreateResult(data, bean, results, i) || foundErrors;
	                }
	                i++;
                }
	        }
        }
		if(foundErrors)
		{
			data.setMessage(sysMsg.getSystemMessage("formError"));
			return Outcome.error(this,"store");
		}
		
		try
		{
			beforeStoreBeans(data, beans);
			Outcome outcome = storeBeans(data, beans);
			afterStoreBeans(data, beans);
    		data.setMessage(sysMsg.getSystemMessage("stored"));
			return outcome;
		}
		catch(Throwable t)
		{
			log.error("Error storing bean", t);
			data.setMessage(sysMsg.getSystemMessage("storeError"));
			return Outcome.error(this,"store");
		}
	}

	private boolean validateAndCreateResult(RunData data, PersistentBean bean, List<Result> results, int index) 
		throws Exception
	{
		ValidatorContext vCtx = runValidator(data, bean);
		boolean foundErrors = vCtx.getErrorCount() > 0;
		Result result = createResult(bean, vCtx,index); 
		results.add(result);
		return foundErrors;
	}
	
	protected void beforeStoreBeans(RunData data, List<PersistentBean> beans)
    	throws Exception
    {}

	protected void afterStoreBeans(RunData data, List<PersistentBean> beans)
		throws Exception
	{}

	protected Outcome storeBeans(RunData data, List<PersistentBean> beans) 
		throws Exception
    {
		for (PersistentBean bean : beans)
		{
			storeObject(data, bean);
		}
		return Outcome.success(this,"store");
    }

	protected abstract List createFromRequest(RunData data)
		throws Exception;

}