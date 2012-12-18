package br.com.ibnetwork.guara.modules;

public class ModuleLoaderException
	extends RuntimeException
{

    public ModuleLoaderException(String message)
    {
        super(message);
    }

    public ModuleLoaderException(String message, Throwable t)
    {
        super(message,t);
    }
}
