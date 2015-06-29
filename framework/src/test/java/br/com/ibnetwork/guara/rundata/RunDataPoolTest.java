package br.com.ibnetwork.guara.rundata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import br.com.ibnetwork.guara.test.GuaraTestCase;
import xingu.container.Inject;

public class RunDataPoolTest
	extends GuaraTestCase
{
	@Inject
    RunDataPool runDataPool;
    
	@Test
    public void testCreateRunData()
	    throws Exception
	{
	    Map<String, String> map = new HashMap<String, String>();
	    map.put(PageInfo.ACTION, "Action");
	    map.put(PageInfo.SCREEN, "Screen");
	    map.put(PageInfo.LAYOUT, "Layout");
	    RunData data = runData(map, "/template/dir.Template");
        assertNotNull(data.getContext());
        assertNotNull(data.getRequest());
        PageInfo pageInfo = data.getPageInfo();
        assertEquals("dir.Template", pageInfo.getTemplate());
        assertEquals("Layout", pageInfo.getLayoutTemplate());
        assertEquals("Screen", pageInfo.getScreenName());
        assertEquals("Action", pageInfo.getActionName());
    }
}

