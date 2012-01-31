package br.com.ibnetwork.guara.modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.ibnetwork.guara.modules.actions.ActionTestModule;
import br.com.ibnetwork.guara.modules.otherscreens.OtherScreenTestModule;
import br.com.ibnetwork.guara.modules.otherscreens.subpackage.InOtherSubpackage;
import br.com.ibnetwork.guara.modules.screens.ConcreteModule;
import br.com.ibnetwork.guara.modules.screens.ScreenTestModule;
import br.com.ibnetwork.guara.modules.screens.subpackage.InSubpackage;
import br.com.ibnetwork.guara.test.GuaraTestCase;
import br.com.ibnetwork.xingu.container.Inject;

public class ModuleLoaderTest
	extends GuaraTestCase
{
	@Inject
	private ModuleLoader loader;
        
	@Test
	public void testLoadScreen()
    	throws Exception
    {
        Module module = loader.loadModule("ScreenTestModule",ModuleLoader.SCREEN_TYPE);
        assertTrue(module instanceof ScreenTestModule);
        assertEquals("ScreenTestModule",module.getName());
    }

	@Test
    public void testLoadScreenInSubpackage()
    	throws Exception
    {
        Module module = loader.loadModule("subpackage.InSubpackage",ModuleLoader.SCREEN_TYPE);
        assertEquals("subpackage.InSubpackage",module.getName());
        assertTrue(module instanceof InSubpackage);
    }
    
	@Test
    public void testLoadScreenInOtherPackage()
    	throws Exception
    {
        Module module = loader.loadModule("OtherScreenTestModule",ModuleLoader.SCREEN_TYPE);
        assertTrue(module instanceof OtherScreenTestModule);
    }

	@Test
    public void testLoadScreenInOtherSubpackage()
		throws Exception
	{
        Module module = loader.loadModule("subpackage.InOtherSubpackage",ModuleLoader.SCREEN_TYPE);
        assertTrue(module instanceof InOtherSubpackage);
	}

    @Test
	public void testDefaultScreenLoad()
		throws Exception
	{
        Module module = loader.loadModule("FooScreen",ModuleLoader.SCREEN_TYPE);
        assertEquals("FooScreen",module.getName());
        assertTrue(module instanceof br.com.ibnetwork.guara.modules.screens.DefaultModuleForTest);
	}

    @Test
    public void testDefaultScreenLoadInSubpackage()
		throws Exception
	{
        Module module = loader.loadModule("subpackage.FooScreen",ModuleLoader.SCREEN_TYPE);
        assertTrue(module instanceof br.com.ibnetwork.guara.modules.screens.subpackage.DefaultModuleForTest);
	}

    @Test
    public void testDefaultScreenLoadInOtherSubpackage()
		throws Exception
	{
        Module module = loader.loadModule("other.FooScreen",ModuleLoader.SCREEN_TYPE);
        assertTrue(module instanceof br.com.ibnetwork.guara.modules.screens.DefaultModuleForTest);
	}

    @Test
    public void testCacheOnRecurse()
    	throws Exception
    {
        Module module1 = loader.loadModule("subpackage.sub.FooScreen",ModuleLoader.SCREEN_TYPE);
        Module module2 = loader.loadModule("subpackage.sub.FooScreen",ModuleLoader.SCREEN_TYPE);
        assertSame(module1,module2);
    }
    
    @Test
    public void testLoadAction()
    	throws Exception
    {
        Module module = loader.loadModule("ActionTestModule",ModuleLoader.ACTION_TYPE);
        assertTrue(module instanceof ActionTestModule);
    }

    @Test
    public void testCache()
    	throws Exception
    {
        Module module1 = loader.loadModule("ActionTestModule",ModuleLoader.ACTION_TYPE);
        Module module2 = loader.loadModule("ActionTestModule",ModuleLoader.ACTION_TYPE);
        assertSame(module1,module2);
    }
    
    @Test
    public void testAbstractModule()
    	throws Exception
    {
        Module module = loader.loadModule("AbstractModule",ModuleLoader.SCREEN_TYPE);
        assertTrue("shoulf have created a concrete class",module instanceof ConcreteModule);
    }
    
    @Test
    public void testThreadSafeModule()
    	throws Exception
    {
    	Module m1 = loader.loadModule("ThreadSafeModule",ModuleLoader.ACTION_TYPE);
    	Module m2 = loader.loadModule("ThreadSafeModule",ModuleLoader.ACTION_TYPE);
    	assertNotSame(m1, m2);
    }
}
