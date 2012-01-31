package br.com.ibnetwork.guara.pull;

import static org.junit.Assert.assertEquals;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.ibnetwork.guara.pull.tools.LinkTool;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.test.GuaraTestCase;

public class LinkToolTest
	extends GuaraTestCase
{
    private LinkTool tool;
    
    private String BASE_ADDRESS = "http://localhost:8080/guara";
    
    @Before
    public void setUp() 
    	throws Exception
    {
        super.setUp();
        RunData data = runData();
        tool = new LinkTool();
        Configuration conf = new DefaultConfiguration("tool");
        tool.configure(conf);
        tool.refresh(data);
    }
    
    @After
    public void tearDown() 
    	throws Exception
    {
        super.tearDown();
        tool = null;
    }
    
    @Test
    public void testBaseAddress()
    {
        String url = tool.toString();
        assertEquals(BASE_ADDRESS,url);
    }

    @Test
    public void testSetNullTemplate()
    {
        String url = tool.setTemplate(null).toString();
        assertEquals(BASE_ADDRESS,url);
    }

    @Test
    public void testSetEmpltyTemplate()
    {
        String url = tool.setTemplate("  ").toString();
        assertEquals(BASE_ADDRESS,url);
    }

    @Test
    public void testSetTemplate()
    {
        String url = tool.setTemplate("Template").toString();
        assertEquals(BASE_ADDRESS+"/Template",url);
        
        url = tool.setTemplate("dir.Template").toString();
        assertEquals(BASE_ADDRESS+"/dir/Template",url);
    }

    @Test
    public void testSetAction()
    {
        String url = tool.setAction("Action").toString();
        assertEquals(BASE_ADDRESS+"?action=Action",url);
    }

    @Test
    public void testSetScreen()
    {
        String url = tool.setScreen("Screen").toString();
        assertEquals(BASE_ADDRESS+"?screen=Screen",url);
    }

    @Test
    public void testSetLayout()
    {
        String url = tool.setLayout("Layout").toString();
        assertEquals(BASE_ADDRESS+"?layout=Layout",url);
    }

    @Test
    public void testSetPipeline()
    {
        String url = tool.setPipeline("Pipeline").toString();
        assertEquals(BASE_ADDRESS+"?pipeline=Pipeline",url);
    }
    
    @Test
    public void testAddQueryData()
    	throws Exception
    {
        String url = tool.addQueryData("var","value").toString();
        assertEquals(BASE_ADDRESS+"?var=value",url);
    }

    @Test
    public void testAddQueryDataMultipleVars()
		throws Exception
	{
        String url = tool.addQueryData("var","value")
        	.addQueryData("var2","value2").toString();
        assertEquals(BASE_ADDRESS+"?var=value&var2=value2",url);
	}

    @Test
    public void testEncodingOnQueryData()
		throws Exception
	{
        String url = tool.addQueryData("var","value value").toString();
        assertEquals(BASE_ADDRESS+"?var=value+value",url);
	}
    
    @Test
    public void testSetTemplateAndQuery()
    {
        String url = tool.setTemplate("Template").add("name", "value").toString();
        assertEquals(BASE_ADDRESS+"/Template?name=value",url);
    }

    @Test
    public void testSetTemplateAndActionAndQuery()
    {
        String url = tool.setTemplate("Template").setAction("Action").add("name", "value").toString();
        assertEquals(BASE_ADDRESS+"/Template?name=value&action=Action",url);
    }
    
//    @Test
//    public void testAddPathInfo()
//    	throws Exception
//    {
//        String url = tool.addPathInfo("var","value").toString();
//        assertEquals(BASE_ADDRESS+"?var=value",url);
//    }
//
//    @Test
//    public void testAddPathInfoMultipleVars()
//		throws Exception
//	{
//        String url = tool.addPathInfo("var","value")
//        	.addPathInfo("var","value").toString();
//        assertEquals(BASE_ADDRESS+"?var=value&var=value",url);
//	}
//
//    @Test
//    public void testAddPathInfoAndQueryData()
//		throws Exception
//	{
//        String url = tool
//        	.addQueryData("var2","value2")
//        	.addPathInfo("var","value")
//        	.toString();
//        assertEquals(BASE_ADDRESS+"?var=value&var2=value2",url);
//	}
}
