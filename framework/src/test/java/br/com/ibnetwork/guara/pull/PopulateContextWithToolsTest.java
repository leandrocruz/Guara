package br.com.ibnetwork.guara.pull;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.ibnetwork.guara.pipeline.FakePipeline;
import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.valve.pull.PopulateContextWithTools;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.rundata.RunDataPool;
import br.com.ibnetwork.guara.test.GuaraTestCase;
import br.com.ibnetwork.xingu.template.Context;

public class PopulateContextWithToolsTest
	extends GuaraTestCase
{
    PopulateContextWithTools valve;
    
    RunDataPool runDataPool;
    
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
        valve = new PopulateContextWithTools();
        getContainer().startLifecycle(valve, null);
        valve.setPipeline(pipeline);
    }
    
    @Test
    public void testGlobalTools()
    	throws Exception
    {
        RunData runData = runData();
        valve.forward(runData);
        Context ctx = runData.getContext();
        assertTrue(ctx.contains("globalTool"));
        ApplicationTool tool1 = (ApplicationTool) ctx.get("globalTool");
        valve.forward(runData);
        ApplicationTool tool2 = (ApplicationTool) ctx.get("globalTool");
        assertSame(tool1,tool2);
    }

    @Test
    public void testRequestTools()
		throws Exception
	{
        RunData runData = runData();
        valve.forward(runData);
        Context ctx = runData.getContext();
        assertTrue(ctx.contains("requestTool"));
        ApplicationTool tool1 = (ApplicationTool) ctx.get("requestTool");
        valve.forward(runData);
        ApplicationTool tool2 = (ApplicationTool) ctx.get("requestTool");
        assertNotSame(tool1,tool2);
	}

    @Test
    public void testSessionTools()
		throws Exception
	{
        System.out.println("TODO: restore tools from session");
	}        
}
