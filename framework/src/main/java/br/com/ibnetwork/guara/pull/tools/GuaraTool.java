package br.com.ibnetwork.guara.pull.tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;

public class GuaraTool 
	extends ApplicationToolSupport 
{
	public String getStackTrace(Throwable t)
	{
		if(t == null)
		{
			return StringUtils.EMPTY;
		}
		String trace = ExceptionUtils.getStackTrace(t);
		return StringUtils.abbreviate(trace, 1500);
	}
}
