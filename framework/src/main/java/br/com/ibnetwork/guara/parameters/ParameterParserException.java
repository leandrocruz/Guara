package br.com.ibnetwork.guara.parameters;

import br.com.ibnetwork.guara.GuaraException;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class ParameterParserException
        extends GuaraException
{

    public ParameterParserException(String message, Throwable t)
    {
        super(message, t);
    }

}
