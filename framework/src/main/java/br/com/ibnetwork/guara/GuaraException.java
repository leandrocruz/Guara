package br.com.ibnetwork.guara;

public class GuaraException
	extends RuntimeException
{

    public GuaraException(String message)
    {
        super(message);
    }

    public GuaraException(String message, Throwable t)
    {
        super(message,t);
    }

}
