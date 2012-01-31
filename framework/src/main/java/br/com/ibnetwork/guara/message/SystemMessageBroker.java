package br.com.ibnetwork.guara.message;

import java.util.Locale;

public interface SystemMessageBroker
{
	Locale getDefaultLocale();

	boolean isLenient();
	
	SystemMessage getSystemMessage(String msgId)
		throws SystemMessageException;

	SystemMessage getSystemMessage(String msgId,Locale locale)
		throws SystemMessageException;
}
