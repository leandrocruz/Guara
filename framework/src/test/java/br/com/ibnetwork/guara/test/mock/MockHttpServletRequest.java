package br.com.ibnetwork.guara.test.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.iterators.IteratorEnumeration;

import br.com.ibnetwork.xingu.utils.StringUtils;

public class MockHttpServletRequest
	implements HttpServletRequest
{
    private Map<String, String> map;
    
    private String pathInfo;
    
    private HttpSession session;
    
    public MockHttpServletRequest(Map<String, String> map, String pathInfo)
    {
        this.map = map;
        this.pathInfo = pathInfo;
    }
    
    public String getAuthType()
    {
        return null;
    }

    public Cookie[] getCookies()
    {
        return null;
    }

    public long getDateHeader(String arg0)
    {
        return 0;
    }

    public String getHeader(String arg0)
    {
        return null;
    }

    public Enumeration getHeaders(String arg0)
    {
        return null;
    }

    public Enumeration getHeaderNames()
    {
        return null;
    }

    public int getIntHeader(String arg0)
    {
        return 0;
    }

    public String getMethod()
    {
        return "POST";
    }

    public String getPathInfo()
    {
        return pathInfo;
    }

    public String getPathTranslated()
    {
        return null;
    }

    public String getContextPath()
    {
        return "/guara";
    }

    public String getQueryString()
    {
        StringBuffer sb = new StringBuffer();
        Set<String> keys = map.keySet();
        int i = 0;
        
        for (String key : keys)
        {
            sb.append(key).append(StringUtils.EQUALS).append(map.get(key));
            i++;
            if(i < keys.size())
            {
                sb.append(StringUtils.AND);
            }
        }
        return sb.toString();
    }

    public String getRemoteUser()
    {
        return null;
    }

    public boolean isUserInRole(String arg0)
    {
        return false;
    }

    public Principal getUserPrincipal()
    {
        return null;
    }

    public String getRequestedSessionId()
    {
        return null;
    }

    public String getRequestURI()
    {
        return null;
    }

    public StringBuffer getRequestURL()
    {
        return null;
    }

    public String getServletPath()
    {
        return "/servlet";
    }

    public HttpSession getSession(boolean arg0)
    {
        return null;
    }

    public HttpSession getSession()
    {
        if(session == null)
        {
            session = new MockHttpSession();
        }
        return session;
    }

    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    public Object getAttribute(String arg0)
    {
        return null;
    }

    public Enumeration getAttributeNames()
    {
        return null;
    }

    public String getCharacterEncoding()
    {
        return null;
    }

    public void setCharacterEncoding(String arg0)
            throws UnsupportedEncodingException
    {}

    public int getContentLength()
    {
        return 0;
    }

    public String getContentType()
    {
        return null;
    }

    public ServletInputStream getInputStream()
            throws IOException
    {
        return null;
    }

    public String getParameter(String name)
    {
        return map.get(name);
    }

    public Enumeration getParameterNames()
    {
        return new IteratorEnumeration(map.keySet().iterator());
    }

    public String[] getParameterValues(String arg0)
    {
    	Object obj = map.get(arg0);
    	String[] result;
    	if(obj instanceof String) 
    	{
    		String s = (String) obj;
    		result = new String[]{s};
    	}
    	else 
    	{
    		result = (String[]) obj;
    	}
        return result;
    }

    public Map getParameterMap()
    {
        return null;
    }

    public String getProtocol()
    {
        return null;
    }

    public String getScheme()
    {
        return "http";
    }

    public String getServerName()
    {
        return "localhost";
    }

    public int getServerPort()
    {
        return 8080;
    }

    public BufferedReader getReader()
            throws IOException
    {
        return null;
    }

    public String getRemoteAddr()
    {
        return null;
    }

    public String getRemoteHost()
    {
        return null;
    }

    public void setAttribute(String arg0, Object arg1)
    {}

    public void removeAttribute(String arg0)
    {}

    public Locale getLocale()
    {
        return null;
    }

    public Enumeration getLocales()
    {
        return null;
    }

    public boolean isSecure()
    {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String arg0)
    {
        return null;
    }

    public String getRealPath(String arg0)
    {
        return null;
    }

}
