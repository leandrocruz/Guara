package br.com.ibnetwork.guara.myapp.om;

import br.com.ibnetwork.guara.annotation.ColumnInfo;
import br.com.ibnetwork.guara.annotation.InputInfo;

public class BeanSupport
	implements Bean
{
	@ColumnInfo(name="ID",jdbcType=ColumnInfo.Type.BIGINT,primaryKey=true,nullable=false)	
	@InputInfo(inputType="hidden", order=0, label = "Id")
	protected long id;
	
	public long getId()
    {
	    return id;
    }

	public void setId(long id)
    {
		this.id = id;
    }

}
