package br.com.ibnetwork.guara.myapp.om;

import xingu.store.PersistentBean;

public class Bean
    implements PersistentBean
{
    private static final long serialVersionUID = 1L;

    private long _id;
	
	public Bean()
    {}

	public Bean(long id)
    {
		_id = id;
    }

	public String getDisplay()
	{
		return null;
	}

	public long getId()
	{
		return _id;
	}

	public void setId(long id)
	{
		_id = id;
	}
}
