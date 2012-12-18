package br.com.ibnetwork.guara;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.guara.pipeline.Pipeline;
import br.com.ibnetwork.guara.pipeline.PipelineManager;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.rundata.RunDataPool;
import br.com.ibnetwork.guara.view.TemplateUtils;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.container.Environment;
import br.com.ibnetwork.xingu.container.impl.EnvironmentImpl;
import xingu.template.Context;
import xingu.template.TemplateEngine;
import xingu.template.TemplateEngineException;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class Guara 
	extends HttpServlet
{
    private static final String CONTEXT_KEY = "guara.servlet";

    private static Logger log = LoggerFactory.getLogger(Guara.class);    
    
    private static ServletContext context;
    
    private Pipeline defaultPipeline;

    private Map<String, Pipeline> pipelineMap;
    
    private Container container;

    private RunDataPool runDataPool;
    
    private TemplateEngine engine;
    
    private String onErrorTemplate;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
        doGet(request,response);
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
        RunData runData = null;
        try
        {
            //1. create runData
            runData = runDataPool.createFrom(request,response,getServletConfig());
            
            //2. select pipeline by name 
            Pipeline pipeline = getPipelineFromRequest(runData);
            
            //3. execute pipeline
            pipeline.execute(runData);    
        }
        catch(Throwable t)
        {
            if(runData != null)
            {
                runData.setError(t);
                Context ctx = runData.getContext();
                ctx.put("throwable",t);
                ctx.put("stackTrace",ExceptionUtils.getStackTrace(t));
                if(t instanceof TemplateEngineException)
                {
                    ctx.put("isTemplateError",true);
                }
                else
                {
                    ctx.put("isTemplateError",false);
                }
                TemplateUtils.renderOutput(engine,runData,onErrorTemplate);
            }
        	log.error("error executing guara pipeline",t);
        }
    }


    private Pipeline getPipelineFromRequest(RunData runData)
    {
        Pipeline pipeline = runData.getPipeline();
        return pipeline != null ? pipeline : defaultPipeline;
    }

    public void init(ServletConfig config) 
    	throws ServletException
    {
        super.init(config);
        context = config.getServletContext();
        String fileName = config.getInitParameter("pulgaConfiguration");
        if(fileName == null)
        {
            fileName = "pulga.xml";
        }
        fileName = getRealPath(fileName);
        log.info("Initializing pulga using file: {}", fileName);
        try
        {
        	Environment env = new EnvironmentImpl();
        	env.put("${app.root}", getRealPath(StringUtils.SLASH));
            container = ContainerUtils.getContainer(fileName);
            engine = container.lookup(TemplateEngine.class);
            onErrorTemplate = engine.getOnErrorTemplate();
            PipelineManager pipelineManager = container.lookup(PipelineManager.class);
            pipelineMap = pipelineManager.getPipelineMap();
            defaultPipeline = (Pipeline) pipelineMap.get(Pipeline.DEFAULT_PIPELINE_NAME);
            runDataPool = (RunDataPool) container.lookup(RunDataPool.class);
            context.setAttribute(CONTEXT_KEY,this);
        }
        catch (Exception e)
        {
            throw new ServletException("Error creating container. System is unusable",e);
        }
    }

    public void destroy()
    {
        log.info("Stopping guara");
        super.destroy();
        container.stop();
    }
    
    public static String getRealPath(String resourceName)
    {
        if(resourceName == null)
        {
            return null;
        }
        if(context == null)
        {
        	/* junit/maven */
        	throw new IllegalArgumentException("Context is null: "+resourceName);
        }
        String path = context.getRealPath(resourceName); 
        return path;
    }
    
    public static URL getResource(String resourceName)
    {
    	try
        {
	        return context.getResource(resourceName);
        }
        catch (MalformedURLException e)
        {
	        log.error("Error translation resource: "+resourceName, e);
	        return null;
        }
    }
}
