package br.com.ibnetwork.guara.message.impl;

import java.text.MessageFormat;

import br.com.ibnetwork.guara.message.SystemMessage;
import br.com.ibnetwork.xingu.utils.ArrayUtils;


public class SystemMessageImpl 
	implements SystemMessage
{
	private String id;
	
	private String text;
	
	private String type;

	public SystemMessageImpl(String id, String text,String type)
	{
		this.id = id;
		this.text = text;	
		this.type = type;
	}

    public String getId()
    {
        return id;
    }

	public String getType()
	{
		return type;
	}

    public String getText(Object[] arguments)
    {
        arguments = ArrayUtils.replaceNulls(arguments,"");
		String result = MessageFormat.format(text,arguments);
		return result;
    }

    public String getText()
    {
        return text;
    }
}
