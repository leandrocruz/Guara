package br.com.ibnetwork.guara.parameters;

import javax.servlet.http.HttpServletRequest;

public interface ParameterParserBuilder
{
    Parameters createParameterParser(HttpServletRequest request)
    	throws Exception;
}
