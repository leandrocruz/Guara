package br.com.ibnetwork.guara.injector;

import java.util.Date;
import java.util.List;

import br.com.ibnetwork.guara.modules.ModuleSupport;

public class MyModule
	extends ModuleSupport
{
	private long id;

	private String s;
	
	private Date sampleDate;
	
	private Date otherDate;

	private String nullValue;
	
	private Pojo pojo1;
	
	private Pojo pojo2;
	
	private List<Pojo> pojos;
	
	public long getId()
    {
    	return id;
    }

	public void setId(long id)
    {
    	this.id = id;
    }

	public String getS()
    {
    	return s;
    }

	public void setS(String string)
    {
    	this.s = string;
    }

	public Date getSampleDate()
    {
    	return sampleDate;
    }

	public void setSampleDate(Date sampleDate)
    {
    	this.sampleDate = sampleDate;
    }

	public Date getOtherDate()
    {
    	return otherDate;
    }

	public void setOtherDate(Date otherDate)
    {
    	this.otherDate = otherDate;
    }

	public String getNullValue()
    {
	    return nullValue;
    }

	public void setNullValue(String nullValue)
    {
    	this.nullValue = nullValue;
    }

	public Pojo getPojo1()
    {
    	return pojo1;
    }

	public void setPojo1(Pojo pojo)
    {
    	this.pojo1 = pojo;
    }
	
	public Pojo getPojo2()
    {
    	return pojo2;
    }

	public void setPojo2(Pojo pojo2)
    {
    	this.pojo2 = pojo2;
    }

	public List<Pojo> getPojos()
    {
    	return pojos;
    }

	public void setPojos(List<Pojo> pojos)
    {
    	this.pojos = pojos;
    }
}
