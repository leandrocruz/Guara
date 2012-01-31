package br.com.ibnetwork.guara.app.crud;

import java.io.Serializable;

import br.com.ibnetwork.guara.annotation.ColumnInfo;
import br.com.ibnetwork.guara.annotation.InputInfo;
import xingu.store.PersistentBean;

public class BeanSupport
    implements PersistentBean, Serializable
{
    private static final long serialVersionUID = 1L;

	@ColumnInfo(name="ID", jdbcType=ColumnInfo.Type.BIGINT, primaryKey=true, nullable=false)
	@InputInfo(label = "ID", inputType="hidden", includeOnDetail=false, order=0)
	protected long id;
	
	@ColumnInfo(ignore=true)
	@InputInfo(includeOnDetail=false, includeOnListing=false, label = "ID")
	protected String display;
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Override
    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
		if(this == obj)
		{
			return true;
		}
		if(getClass().equals(obj.getClass()))
		{
			PersistentBean other = (PersistentBean) obj;
			return getId() == other.getId();
		}
		return false;
    }

	@Override
	public int hashCode() 
	{
	    return (int) getId();
	}

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "#" + id;
    }
}
