package br.com.ibnetwork.guara.app.test;

import java.util.Date;

import br.com.ibnetwork.guara.app.crud.BeanSupport;
import br.com.ibnetwork.xingu.validator.ann.ValidateRequired;

public class SampleBean
	extends BeanSupport
{
	@ValidateRequired
	private String name;
	
	private int number;
	
	private Integer numberInteger;
	
	private Date date;
	
	private Object noGetSet; //should be ignored

	public Date getDate()
    {
    	return date;
    }

	public void setDate(Date date)
    {
    	this.date = date;
    }

	public String getName()
    {
    	return name;
    }

	public void setName(String name)
    {
    	this.name = name;
    }

	public int getNumber()
    {
    	return number;
    }

	public void setNumber(int number)
    {
    	this.number = number;
    }

	public Integer getNumberInteger()
    {
    	return numberInteger;
    }

	public void setNumberInteger(Integer numberInteger)
    {
    	this.numberInteger = numberInteger;
    }
}
