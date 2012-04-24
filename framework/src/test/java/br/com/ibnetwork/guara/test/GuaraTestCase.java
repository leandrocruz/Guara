package br.com.ibnetwork.guara.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.Before;

import br.com.ibnetwork.guara.Guara;
import br.com.ibnetwork.guara.modules.ModuleLoader;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.rundata.RunDataPool;
import br.com.ibnetwork.guara.test.mock.MockHttpServletRequest;
import br.com.ibnetwork.guara.test.mock.MockHttpServletResponse;
import br.com.ibnetwork.guara.test.mock.MockServletConfig;
import br.com.ibnetwork.guara.test.mock.MockServletContext;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import xingu.template.Context;
import xingu.template.TemplateEngine;
import br.com.ibnetwork.xingu.utils.FSUtils;

public abstract class GuaraTestCase
    extends XinguTestCase
{
	private static Guara guara;
	
	@Inject
    protected RunDataPool runDataPool;
    
	@Inject
    protected ModuleLoader loader;
    
	@Inject
    protected TemplateEngine templateEngine;
	
	protected Guara getGuara()
		throws Exception
	{
		if(guara == null)
		{
	    	String webAppRoot = getAppRootDir();
	    	if(webAppRoot == null)
	    	{
	    		webAppRoot = FSUtils.load(".");
	    	}
	    	Map parameters = getConfigParameters();
	    	ServletContext ctx = new MockServletContext(webAppRoot);
	    	ServletConfig config = new MockServletConfig(ctx,parameters);
	    	guara = new Guara();
	    	guara.init(config);
		}
		return guara;
	}

    protected String getContainerFile()
    {
    	return "guaraTest.xml";
    }
    
    protected void backupResources() 
    	throws Exception
    {
    	/* nothing to do */
    }

    protected void restoreResources() 
    	throws Exception 
    {
    	/* nothing to do */
    }

    protected String getAppRootDir()
    	throws Exception
    {
    	/* nothing to do */
    	return null;
    }

    protected Map getConfigParameters()
    {
    	Map parameters = new HashMap();
    	parameters.put("pulgaConfiguration", getContainerFile());
    	return parameters;
    }

    @Before
    public void setUp()
    	throws Exception
    {
    	getGuara();
        backupResources();
    }

    @After
    public void tearDown()
		throws Exception
	{
        restoreResources();
	}

    protected Context createContext()
    {
        return templateEngine.createContext();
    }
    
    protected RunData runData()
        throws Exception
    {
        return runData("");
    }

    protected RunData runData(String path) 
        throws Exception
    {
        return runData(new HashMap<String, String>(), path);
    }

    protected RunData runData(Map<String, String> queryData, String path) 
        throws Exception
    {
        return runDataPool.createFrom(new MockHttpServletRequest(queryData, path), new MockHttpServletResponse(), null);
    }
}