package br.com.ibnetwork.guara.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.ibnetwork.guara.pipeline.impl.PipelineManagerImpl;
import br.com.ibnetwork.guara.pipeline.valve.AbortValve;
import br.com.ibnetwork.guara.pipeline.valve.TestValve;
import br.com.ibnetwork.guara.test.GuaraTestCase;


/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class PipelineTest
	extends GuaraTestCase
{
    private PipelineManager pipelineManager;
    
    @Before
    public void setUp() 
    	throws Exception
    {
        PipelineManagerImpl instance = new PipelineManagerImpl();
        DefaultConfiguration conf = new DefaultConfiguration("component");
        DefaultConfiguration child = new DefaultConfiguration("file");
        child.setAttribute("name","pipelineTest.xml");
        conf.addChild(child);
        getContainer().startLifecycle(instance, conf);
        pipelineManager = instance;
    }

    @After
    public void tearDown() 
    	throws Exception
    {
        pipelineManager = null;
    }
    
    @Test
    public void testPipelineValves()
    	throws Exception
    {
        Pipeline pipeline = pipelineManager.getPipeline("test");
        assertEquals(2,pipeline.getValves().length);
        pipeline.execute(null);
        Valve[] valves = pipeline.getValves();
        for (int i = 0; i < valves.length; i++)
        {
            TestValve valve = (TestValve) valves[i];
            assertTrue(valve.hasExecuted());
            assertFalse(valve.isComposed());
            assertTrue(valve.isConfigured());
        }
    }

    @Test
    public void testValveConfiguration()
    	throws Exception
    {
        Pipeline pipeline = pipelineManager.getPipeline(Pipeline.DEFAULT_PIPELINE_NAME);
        TestValve valve = (TestValve) pipeline.getValveByName("d1");
        assertEquals("value1",valve.getConfiguration().getChild("some").getAttribute("key"));    
        valve = (TestValve) pipeline.getValveByName("d2");
        assertEquals("value2",valve.getConfiguration().getChild("some").getAttribute("key"));
    }
    
    @Test
    public void testAbortPipeline()
        throws Exception
    {
        Pipeline pipeline = pipelineManager.getPipeline("testAbort");
        pipeline.execute(null);
        AbortValve v1 = (AbortValve) pipeline.getValveByName("v1");
        assertTrue(v1.hasExecuted());
        TestValve v2 = (TestValve) pipeline.getValveByName("v2");
        assertFalse(v2.hasExecuted());
    }
}
