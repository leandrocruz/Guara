package br.com.ibnetwork.guara.message;

public interface SystemMessage
{
    String DEFAULT_MESSAGE_TYPE = "info";
    
    String getId();
	
	String getType();
	
	String getText(Object[] arguments);
	
	String getText();
}
