package br.com.ibnetwork.guara;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.guara.metadata.handler.MacroReplacer;
import br.com.ibnetwork.guara.metadata.impl.AjaxScreen;
import br.com.ibnetwork.guara.metadata.impl.Bean;
import br.com.ibnetwork.guara.metadata.impl.BeanDao;
import br.com.ibnetwork.guara.metadata.impl.ControllerSupport;
import br.com.ibnetwork.guara.metadata.impl.CrudTest;
import br.com.ibnetwork.guara.metadata.impl.EditScreen;
import br.com.ibnetwork.guara.metadata.impl.EmptyController;
import br.com.ibnetwork.guara.metadata.impl.FreemarkerBeanMacro;
import br.com.ibnetwork.guara.metadata.impl.HsqlDb;
import br.com.ibnetwork.guara.metadata.impl.IndexScreen;
import br.com.ibnetwork.guara.metadata.impl.ListScreen;
import br.com.ibnetwork.guara.metadata.impl.ShowScreen;
import br.com.ibnetwork.guara.metadata.impl.SqlMap;
import br.com.ibnetwork.guara.metadata.impl.Tool;

@Ignore
public class GeneratorTest
	extends GeneratorTestSupport
{
	
//	public static TestSuite suite()
//	{
//		TestSuite suite = new TestSuite();
//		suite.addTest(new GeneratorTest("testGenerateHsqlDb"));
//		suite.addTest(new GeneratorTest("testFreemarkerMacro"));
//		return suite;
//	}

    @Test
    public void testGenerateBean()
		throws Exception
	{
		callGenerator(createMeta(Bean.class, "om.Sample"));
	}
	
    @Test
	public void testGenerateIbatisConfig()
		throws Exception
	{
		callGenerator(createMeta(SqlMap.class));
	}
	
    @Test
	public void testGenerateCrudTest()
		throws Exception
	{
		callGenerator(createMeta(CrudTest.class));
	}

    @Test
	public void testGenerateController()
		throws Exception
	{
		callGenerator(createMeta(ControllerSupport.class));
		callGenerator(createMeta(EmptyController.class));
	}
	
    @Test
	public void testGenerateBeanDao()
		throws Exception
	{
		callGenerator(createMeta(BeanDao.class));
	}
	
    @Test
	public void testGenerateTool()
		throws Exception
	{
		callGenerator(createMeta(Tool.class));
	}
	
    @Test
	public void testGenerateAjaxScreen()
		throws Exception	
	{
		callGenerator(createMeta(AjaxScreen.class));
	}

    @Test
	public void testGenerateEditScreen()
		throws Exception
	{
		callGenerator(createMeta(EditScreen.class));
	}
	
    @Test
	public void testGenerateIndexScreen()
		throws Exception
	{
		callGenerator(createMeta(IndexScreen.class));
	}

    @Test
	public void testGenerateListScreen()
		throws Exception
	{
		callGenerator(createMeta(ListScreen.class));
	}

    @Test	
	public void testGenerateShowScreen()
		throws Exception
	{
		callGenerator(createMeta(ShowScreen.class));
	}
	
    @Test
	public void testGenerateHsqlDb()
		throws Exception
	{
		callGenerator(createMeta(HsqlDb.class));
	}
	
    @Test
	public void testFreemarkerMacro()
		throws Exception
	{
		callGenerator(createMeta(FreemarkerBeanMacro.class,"om.OtherBean",new MacroReplacer()));
	}
	
}
