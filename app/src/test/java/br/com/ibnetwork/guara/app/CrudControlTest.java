package br.com.ibnetwork.guara.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ibnetwork.guara.app.modules.Result;
import br.com.ibnetwork.guara.app.modules.actions.TestSingleCrud;
import br.com.ibnetwork.guara.app.test.SampleBean;
import br.com.ibnetwork.guara.modules.ModuleLoader;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.test.GuaraTestCase;
import br.com.ibnetwork.xingu.template.Context;
import xingu.validator.ValidatorContext;

public class CrudControlTest
    extends GuaraTestCase
{
	
	@Override
    protected String getContainerFile()
    {
	    return "pulgaTest.xml";
    }
	
	public void testFoo()
	{}
	
	public void testStore()
		throws Exception
	{
		TestSingleCrud module = (TestSingleCrud) loader.loadModule("TestSingleCrud", ModuleLoader.ACTION_TYPE);
		Map map = new HashMap();
		RunData data = runData("/");
		Context ctx = createContext();
		module.store(data, ctx);
		List<Result> results = (List<Result>) ctx.get("storeResults");
		assertEquals(1, results.size());
		Result result = results.get(0);
		Object bean = result.getBean();
		assertTrue(bean instanceof SampleBean);
		ValidatorContext vCtx = result.getValidatorContext();
		assertEquals(1,vCtx.getErrorCount());
	}
}
