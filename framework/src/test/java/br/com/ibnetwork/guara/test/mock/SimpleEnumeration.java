package br.com.ibnetwork.guara.test.mock;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

public class SimpleEnumeration
    implements Enumeration
{
    private Iterator it;
    
    public SimpleEnumeration(Iterator it)
    {
        this.it = it;
    }

    public SimpleEnumeration(Collection coll)
    {
        this.it = coll.iterator();
    }
    
    public boolean hasMoreElements()
    {
        return it.hasNext();
    }

    public Object nextElement()
    {
        return it.next();
    }
}
