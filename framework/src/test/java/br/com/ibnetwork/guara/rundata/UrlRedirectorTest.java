package br.com.ibnetwork.guara.rundata;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import br.com.ibnetwork.guara.test.GuaraTestCase;
import xingu.container.Inject;

public class UrlRedirectorTest
    extends GuaraTestCase
{
    @Inject
    private UrlRedirector redirector;

    @Test
    public void testRedirectPath()
        throws Exception
    {
        String location = redirector.locationFor(runData("/template/Template"));
        assertEquals("http://localhost:8080/guara/Template", location);

        location = redirector.locationFor(runData("/template/dir.Template"));
        assertEquals("http://localhost:8080/guara/dir/Template", location);

        location = redirector.locationFor(runData("/template/Template/action"));
        assertEquals("http://localhost:8080/guara/Template", location);

        location = redirector.locationFor(runData("/template/Template/action/Action"));
        assertEquals("http://localhost:8080/guara/Template?action=Action", location);
    }

    @Test
    public void testRedirectPathWithQueryData()
        throws Exception
    {
        Map<String, String> query = new HashMap<String, String>();
        query.put("key", "value");
        String location = redirector.locationFor(runData(query, "/template/Template"));
        assertEquals("http://localhost:8080/guara/Template?key=value", location);

        location = redirector.locationFor(runData(query, "/template/dir.Template"));
        assertEquals("http://localhost:8080/guara/dir/Template?key=value", location);

        location = redirector.locationFor(runData(query, "/template/Template/action"));
        assertEquals("http://localhost:8080/guara/Template?key=value", location);

        location = redirector.locationFor(runData(query, "/template/Template/action/Action"));
        assertEquals("http://localhost:8080/guara/Template?key=value&action=Action", location);
    }
}
