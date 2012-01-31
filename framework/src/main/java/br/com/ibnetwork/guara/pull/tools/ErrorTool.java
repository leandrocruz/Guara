package br.com.ibnetwork.guara.pull.tools;

import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;

/**
 * @author leandro
 */
public class ErrorTool 
	extends ApplicationToolSupport
{
    public String forceError()
    {
        throw new UnsupportedOperationException("This error was forced");
    }
}
