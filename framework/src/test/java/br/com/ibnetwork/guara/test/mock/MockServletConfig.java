package br.com.ibnetwork.guara.test.mock;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class MockServletConfig
    implements ServletConfig
{
    private ServletContext ctx;
    
    private Map<String, String> map;
    
    public MockServletConfig(ServletContext ctx, Map parameters)
    {
        this.ctx = ctx;
        this.map = parameters;
    }
    
    public String getServletName()
    {
        return "guara";
    }

    public ServletContext getServletContext()
    {
        return ctx;
    }

    public String getInitParameter(String paramName)
    {
    	return map.get(paramName);
    }

    public Enumeration getInitParameterNames()
    {
        return null;
    }

}
