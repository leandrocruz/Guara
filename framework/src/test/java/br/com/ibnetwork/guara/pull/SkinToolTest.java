package br.com.ibnetwork.guara.pull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.ibnetwork.guara.pull.tools.SkinTool;
import br.com.ibnetwork.guara.test.GuaraTestCase;
import xingu.utils.FSUtils;

public class SkinToolTest
	extends GuaraTestCase
{
    private SkinTool tool;
    
    @Before
    public void setUp() 
    	throws Exception
    {
        super.setUp();
        tool = new SkinTool();
        DefaultConfiguration root = new DefaultConfiguration("root");
        DefaultConfiguration conf = new DefaultConfiguration("properties");
        File file = FSUtils.loadAsFile("skin.props");
        conf.setAttribute("fileName",file.getAbsolutePath());
        root.addChild(conf);
        tool.configure(root);
        tool.initialize();
    }
    
    @After
    public void tearDown() 
    	throws Exception
    {
        tool = null;
    }
    
    @Test
    public void testGetProperty()
    	throws Exception
    {
        String value = tool.get("key");
        assertEquals("value",value);
        assertNull(tool.get("null"));
    }
}
