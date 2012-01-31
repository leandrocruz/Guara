package br.com.ibnetwork.guara.app.modules.actions;

import br.com.ibnetwork.guara.app.modules.GuaraModuleSupport;
import br.com.ibnetwork.guara.rundata.RunData;
import xingu.store.PersistentBean;

public abstract class CrudControlSupport
    extends GuaraModuleSupport
{
	protected void storeObject(RunData data, PersistentBean bean)
	    throws Exception
	{
		beforeStore(data, bean);
		if (bean.getId() > 0)
		{
			beforeUpdate(data, bean);
			doUpdate(bean);
			afterUpdate(data, bean);
		}
		else
		{
			beforeCreate(data, bean);
			doCreate(bean);
			afterCreate(data, bean);
		}
		afterStore(data, bean);
	}

	protected void doCreate(PersistentBean bean)
		throws Exception
	{
		store.store(bean);
	}
	
	protected void doUpdate(PersistentBean bean)
		throws Exception
	{
		store.store(bean);
	}
	
    protected void doDelete(PersistentBean bean)
    	throws Exception
    {
    	store.delete(bean);
    }

    protected void afterDelete(RunData data, PersistentBean bean)
		throws Exception
	{}

    protected void beforeDelete(RunData data, PersistentBean bean)
		throws Exception
	{}
	
	protected void afterStore(RunData data, PersistentBean bean)
	    throws Exception
	{}

	protected void afterCreate(RunData data, PersistentBean bean)
	    throws Exception
	{}

	protected void beforeCreate(RunData data, PersistentBean bean)
	    throws Exception
	{}

	protected void afterUpdate(RunData data, PersistentBean bean)
	    throws Exception
	{}

	protected void beforeUpdate(RunData data, PersistentBean bean)
	    throws Exception
	{}

	protected void beforeStore(RunData data, PersistentBean bean)
	    throws Exception
	{}
}
