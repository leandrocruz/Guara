package br.com.ibnetwork.guara.view;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.junit.Before;
import org.junit.Test;

import br.com.ibnetwork.guara.pipeline.FakePipeline;
import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.valve.view.RenderLayout;
import br.com.ibnetwork.guara.rundata.PageInfo;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.test.GuaraTestCase;
import br.com.ibnetwork.guara.test.mock.MockHttpServletRequest;
import br.com.ibnetwork.guara.test.mock.MockHttpServletResponse;
import br.com.ibnetwork.guara.test.mock.MockServletOutputStream;
import xingu.template.Context;

public class RenderLayoutValveTest
	extends GuaraTestCase
{
    RenderLayout valve;
    
    Pipeline pipeline;
    
    @Before
    public void setUp()
    	throws Exception
    {
    	super.setUp();
        if(valve != null)
        {
            return;
        }
        pipeline = new FakePipeline();
        valve = new RenderLayout();
        valve.setPipeline(pipeline);
        DefaultConfiguration root = new DefaultConfiguration("valve");
        DefaultConfiguration conf = new DefaultConfiguration("onLayoutNotFound");
        conf.setAttribute("template","LayoutNotFound");
        root.addChild(conf);
        conf = new DefaultConfiguration("defaultLayout");
        conf.setAttribute("template","DefaultLayout");
        root.addChild(conf);
        getContainer().startLifecycle(valve, root);
    }

    @Test
    public void testDefaultLayoutFromTemplate()
		throws Exception
	{
        Map<String, String> map = new HashMap<String, String>();
        HttpServletRequest request = new MockHttpServletRequest(map,"/template/test.Bar");
        HttpServletResponse response = new MockHttpServletResponse();
        RunData runData = runDataPool.createFrom(request,response,null);
        valve.forward(runData);
        MockServletOutputStream os = (MockServletOutputStream) response.getOutputStream();
        assertEquals("Default [$screen_placeholder]",os.getContents());
	}

    @Test
    public void testMatchTemplateLayout()
		throws Exception
	{
        Map<String, String> map = new HashMap<String, String>();
        HttpServletRequest request = new MockHttpServletRequest(map,"/template/test.Foo");
        HttpServletResponse response = new MockHttpServletResponse();
        RunData runData = runDataPool.createFrom(request,response,null);
        valve.forward(runData);
        MockServletOutputStream os = (MockServletOutputStream) response.getOutputStream();
        assertEquals("Foo [$screen_placeholder]",os.getContents());
	}

    @Test
    public void testLayoutNotFound()
    	throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(PageInfo.LAYOUT, "Foo");
        HttpServletRequest request = new MockHttpServletRequest(map, null);
        HttpServletResponse response = new MockHttpServletResponse();
        RunData runData = runDataPool.createFrom(request,response,null);
        valve.forward(runData);
        Context ctx = runData.getContext();
        String notFound = (String) ctx.get("layoutTemplateName");
        assertEquals("layouts.Foo",notFound);
        MockServletOutputStream os = (MockServletOutputStream) response.getOutputStream();
        assertEquals("layoutTemplateNotFound: <b>layouts.Foo</b>",os.getContents());
    }
}
