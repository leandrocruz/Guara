package br.com.ibnetwork.guara.test.mock;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MockServletContext
    implements ServletContext
{
	private String webAppRoot;
	
	public MockServletContext(String webAppRoot)
	{
		this.webAppRoot = webAppRoot;
	}
	
    public ServletContext getContext(String arg0)
    {
        return null;
    }

    public int getMajorVersion()
    {
        return 0;
    }

    public int getMinorVersion()
    {
        return 0;
    }

    public String getMimeType(String arg0)
    {
        return null;
    }

    public Set getResourcePaths(String arg0)
    {
        return null;
    }

    public String getRealPath(String resource)
    {
    	if(webAppRoot != null)
    	{
    		return webAppRoot + File.separator + resource;
    	}
    	else
    	{
            URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
            return url == null ? null : url.getFile();
    	}
    }

    public URL getResource(String resourceName) 
    	throws MalformedURLException
    {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        return url;
    }

    public InputStream getResourceAsStream(String resourceName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public RequestDispatcher getNamedDispatcher(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Servlet getServlet(String arg0) 
    	throws ServletException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getServlets()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getServletNames()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void log(String arg0)
    {
    // TODO Auto-generated method stub

    }

    public void log(Exception arg0, String arg1)
    {
    // TODO Auto-generated method stub

    }

    public void log(String arg0, Throwable arg1)
    {
        System.out.println(arg0);
        arg1.printStackTrace();
    }

    public String getServerInfo()
    {
        return null;
    }

    public String getInitParameter(String arg0)
    {
        return null;
    }

    public Enumeration getInitParameterNames()
    {
        return null;
    }

    public Object getAttribute(String arg0)
    {
        return null;
    }

    public Enumeration getAttributeNames()
    {
        return null;
    }

    public void setAttribute(String arg0, Object arg1)
    {
        //ignored
    }

    public void removeAttribute(String arg0)
    {
        //ignored
    }

    public String getServletContextName()
    {
        return null;
    }

}
