package br.com.ibnetwork.guara.injector;

import java.util.Date;

public class Pojo
{
	private long id;
	
	private String name;
	
	private Date sampleDate;
	
	public long getId()
    {
    	return id;
    }

	public void setId(long id)
    {
    	this.id = id;
    }

	public String getName()
    {
    	return name;
    }

	public void setName(String name)
    {
    	this.name = name;
    }

	public Date getSampleDate()
    {
    	return sampleDate;
    }

	public void setSampleDate(Date sampleDate)
    {
    	this.sampleDate = sampleDate;
    }

}
