package br.com.ibnetwork.guara.test.mock;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author leandro
 */
public class MockHttpSession 
	implements HttpSession
{
    private long creationTime;
    
    private Map attributes = new HashMap();
    
    public MockHttpSession()
    {
        creationTime = Calendar.getInstance().getTimeInMillis();
    }
    
    public long getCreationTime()
    {
        return creationTime;
    }

    public String getId()
    {
        return null;
    }

    public long getLastAccessedTime()
    {
        return 0;
    }

    public ServletContext getServletContext()
    {
        return null;
    }

    public void setMaxInactiveInterval(int arg0)
    {
    }

    public int getMaxInactiveInterval()
    {
        return 0;
    }

    public HttpSessionContext getSessionContext()
    {
        return null;
    }

    public Object getAttribute(String key)
    {
        return attributes.get(key);
    }

    public Enumeration getAttributeNames()
    {
        Enumeration e = new SimpleEnumeration(attributes.keySet());
        return e;
    }

    public void setAttribute(String key, Object val)
    {
        attributes.put(key,val);
    }

    public void removeAttribute(String key)
    {
        attributes.remove(key);
    }

    public void invalidate()
    {
        attributes.clear();
    }

    public boolean isNew()
    {
        return false;
    }

    /**
     * @deprecated
     */
    public void putValue(String arg0, Object arg1)
    {
    }

    /**
     * @deprecated
     */
    public void removeValue(String arg0)
    {
    }

    /**
     * @deprecated
     */
    public Object getValue(String arg0)
    {
        return null;
    }

    /**
     * @deprecated
     */
    public String[] getValueNames()
    {
        return null;
    }
}
