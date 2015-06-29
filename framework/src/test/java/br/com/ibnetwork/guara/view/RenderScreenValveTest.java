package br.com.ibnetwork.guara.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.modules.ModuleLoader;
import br.com.ibnetwork.guara.pipeline.FakePipeline;
import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.valve.view.RenderScreen;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.rundata.impl.RunDataImpl;
import br.com.ibnetwork.guara.test.GuaraTestCase;
import xingu.container.Inject;
import xingu.template.Context;

/**
 * @author <a href="mailto:leandro.saad@gmail.com">Leandro Rodrigo Saad Cruz</a>
 */
public class RenderScreenValveTest
	extends GuaraTestCase
{
    RenderScreen valve;
    
    Pipeline pipeline;
    
    @Inject
    private ModuleLoader loader;

//	public static TestSuite suite()
//    {
//    	TestSuite suite = new TestSuite();
//    	suite.addTestSuite(RenderScreenValveTest.class);
//    	suite.addTest(new RenderScreenValveTest("testMappingInherence"));
//    	return suite;
//    }
    
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
        valve = new RenderScreen();
        valve.setPipeline(pipeline);
        DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("valves/renderScreenValveTest.xml");
        Configuration root = builder.build(is);
        getContainer().startLifecycle(valve, root);
    }
    
    @Test
    public void testDefaultTemplate()
    	throws Exception
    {
        RunData runData = runData();
        ((RunDataImpl) runData).setPipeline(pipeline);
        valve.forward(runData);
        Context ctx = runData.getContext();
        String result = (String) ctx.get(RenderScreen.SCREEN_CONTEXT_KEY); 
        assertTrue(result.startsWith("<h1>Welcome to Guara</h1>"));
        assertEquals("Index",runData.getPageInfo().getTemplate());
    }
    
    @Test
    public void testTemplateNotFound()
    	throws Exception
    {
        RunData runData = runData("/template/Foo");
        valve.forward(runData);
        Context ctx = runData.getContext();
        String result = (String) ctx.get(RenderScreen.SCREEN_CONTEXT_KEY); 
        assertEquals("templateNotFound: <b>Foo</b>",result);
        assertEquals("Foo",runData.getPageInfo().getTemplate());
    }

    @Test
    public void testRenderPage()
    	throws Exception
    {
        RunData runData = runData("/template/test.Test");
        Context ctx = runData.getContext();
        ctx.put("var","guara");
        valve.forward(runData);
        String result = (String) ctx.get(RenderScreen.SCREEN_CONTEXT_KEY); 
        assertEquals("This is a test guara",StringUtils.chomp(result));
        assertEquals("test.Test",runData.getPageInfo().getTemplate());
    }
    
    @Test
    public void testModuleOutcome()
        throws Exception 
    {
        Module module = loader.loadModule("test.ActionTest",ModuleLoader.ACTION_TYPE);
        
        //SUCCESS
        renderPageForOutcome(Outcome.success(module),"Outcome(Success.vm): SUCCESS test.ActionTest $outcome.method");

        //SUCCESS: doX()
        renderPageForOutcome(Outcome.success(module,"doX"),"Outcome(Success.vm): SUCCESS test.ActionTest doX");

        //SUCCESS: doY()
        renderPageForOutcome(Outcome.success(module,"doY"),"Outcome(Default.vm): SUCCESS test.ActionTest doY");

        //ERROR
        renderPageForOutcome(Outcome.error(module),"Outcome(Error.vm): ERROR test.ActionTest $outcome.method");

        //ERROR: doX()
        renderPageForOutcome(Outcome.error(module,"doX"),"Outcome(Error.vm): ERROR test.ActionTest doX");

        //ERROR: doY()
        renderPageForOutcome(Outcome.error(module,"doY"),"Outcome(Default.vm): ERROR test.ActionTest doY");

        //SAMPLE_CODE
        renderPageForOutcome(Outcome.unknown("SAMPLE_CODE",module),"Outcome(SampleCode.vm): SAMPLE_CODE test.ActionTest $outcome.method");

        //SAMPLE_CODE: doX()
        renderPageForOutcome(Outcome.unknown("SAMPLE_CODE",module,"doX"),"Outcome(SampleCode.vm): SAMPLE_CODE test.ActionTest doX");

        //SAMPLE_CODE: doY()
        renderPageForOutcome(Outcome.unknown("SAMPLE_CODE",module,"doY"),"Outcome(Default.vm): SAMPLE_CODE test.ActionTest doY");

        //DEFAULT
        renderPageForOutcome(Outcome.unknown("FOO",module),"Outcome(Default.vm): FOO test.ActionTest $outcome.method");

        //DEFAULT: doX()
        renderPageForOutcome(Outcome.unknown("FOO",module,"doX"),"Outcome(Default.vm): FOO test.ActionTest doX");

        //DEFAULT: doY()
        renderPageForOutcome(Outcome.unknown("FOO",module,"doY"),"Outcome(Default.vm): FOO test.ActionTest doY");

        //NULL: render page set on RunData
        renderPageForOutcome(null,"Outcome(Unknown.vm): $outcome.code $outcome.source.name $outcome.method","/template/test.outcome.Unknown");

        //NULL: render page set on RunData
        renderPageForOutcome(Outcome.UNKNOWN,"Outcome(Unknown.vm): UNKNOWN $outcome.source.name $outcome.method","/template/test.outcome.Unknown");

    }

    @Test
    public void testMappingInherence()
    	throws Exception
    {
    	Module module = loader.loadModule("test.ParentModule",ModuleLoader.ACTION_TYPE);
    	renderPageForOutcome(Outcome.success(module,"doX"),"Parent");
    	renderPageForOutcome(Outcome.error(module,"doX"),"Parent");

    	module = loader.loadModule("test.ChildModule",ModuleLoader.ACTION_TYPE);
    	renderPageForOutcome(Outcome.success(module,"doX"),"Child");    	
    	renderPageForOutcome(Outcome.error(module,"doX"),"Parent");
    }

    @Test
    public void testAlias()
        throws Exception
    {
        RunData runData = runData("/big");
        valve.forward(runData);
        String template = runData.getPageInfo().getTemplate();
        assertEquals("bang", template);
    }
    
    @Test
    public void renderIndexOnDirectory()
        throws Exception
    {
        RunData runData = runData("/dir1");
        valve.forward(runData);
        String template = runData.getPageInfo().getTemplate();
        assertEquals("dir1.Index", template);
        assertEquals("Index on dir1", runData.getContext().get(RenderScreen.SCREEN_CONTEXT_KEY));

        runData = runData("/dir1/");
        valve.forward(runData);
        template = runData.getPageInfo().getTemplate();
        assertEquals("dir1.Index", template);
        assertEquals("Index on dir1", runData.getContext().get(RenderScreen.SCREEN_CONTEXT_KEY));

        runData = runData("/dir2");
        valve.forward(runData);
        template = runData.getPageInfo().getTemplate();
        assertEquals("dir2", template);
        assertEquals("templateNotFound: <b>dir2</b>", runData.getContext().get(RenderScreen.SCREEN_CONTEXT_KEY));

        runData = runData("/dir2/");
        valve.forward(runData);
        template = runData.getPageInfo().getTemplate();
        assertEquals("dir2", template);
        assertEquals("templateNotFound: <b>dir2</b>", runData.getContext().get(RenderScreen.SCREEN_CONTEXT_KEY));

    }
    
    private void renderPageForOutcome(Outcome outcome, String expected)
        throws Exception
    {
        renderPageForOutcome(outcome, expected,null);
    }
    
    private void renderPageForOutcome(Outcome outcome, String expected, String template)
        throws Exception
    {
        RunData runData = runData(template);
        runData.setOutcome(outcome);
        valve.forward(runData);
        Context ctx = runData.getContext();
        String result = (String) ctx.get(RenderScreen.SCREEN_CONTEXT_KEY);
        assertEquals(expected,result);
    }
}
