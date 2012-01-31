package br.com.ibnetwork.guara.pull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.guara.test.GuaraTestCase;
import br.com.ibnetwork.xingu.container.Inject;

public class PullManagerTest 
	extends GuaraTestCase
{
    @Inject
	private PullManager pullManager;
    
    @Test
    public void testGetAllGlobalTools()
    	throws Exception
    {
        List all = pullManager.getAllToolHandlers(PullManager.SCOPE_GLOBAL);
        for (Iterator iter = all.iterator(); iter.hasNext();)
        {
            ApplicationToolHandler handler = (ApplicationToolHandler) iter.next();
            assertEquals(PullManager.SCOPE_GLOBAL,handler.getScope());
            Object tool1 = handler.newToolInstance();
            Object tool2 = handler.newToolInstance();
            assertSame(tool1,tool2);
        }
    }
    
    @Test
    @Ignore
    public void testToolConfiguration()
    	throws Exception
    {
        List all = pullManager.getAllToolHandlers(PullManager.SCOPE_REQUEST);
        assertEquals(1,all.size());
        ApplicationToolHandler handler = (ApplicationToolHandler) all.get(0);
        TestTool tool1 = (TestTool) handler.newToolInstance();
        assertTrue(tool1.isConfigured());
        assertEquals("value3",tool1.getConfiguration().getChild("some").getAttribute("key"));
        TestTool tool2 = (TestTool) handler.newToolInstance();
        assertNotSame(tool1,tool2);
    }
}
