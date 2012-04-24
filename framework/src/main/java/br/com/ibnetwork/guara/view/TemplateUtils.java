package br.com.ibnetwork.guara.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.rundata.RunData;
import xingu.template.TemplateEngine;
import xingu.template.Context;
import xingu.template.TemplateEngineException;

public class TemplateUtils 
{
	private static final String DEFAULT_CONTENT_TYPE = "text/html; charset=UTF-8";
    
    private static Log logger = LogFactory.getLog(TemplateUtils.class);

    public static String renderTemplate(TemplateEngine engine, String encoding, Context ctx, String templateName)
    {
        ByteArrayOutputStream bytes = null;
        String result;
        try
        {
            bytes = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(bytes, encoding);
            engine.merge(templateName, ctx, writer);
            writer.flush();
            result = bytes.toString(encoding);
            return result;
        }
        catch (Throwable t)
        {
        	TemplateEngineException tee;
        	if(t instanceof TemplateEngineException)
        	{
        		tee = (TemplateEngineException) t;
        	}
        	else
        	{
        		tee = new TemplateEngineException("Error merging template ["+templateName+"]",t); 
        	}
            throw tee;
        }
        finally
        {
            try
            {
                if (bytes != null)
                {
                    bytes.close();
                }
            }
            catch (IOException e)
            {
                logger.warn("Error closing output stream",e);
            }
        }
    }

    public static void renderOutput(TemplateEngine engine, RunData runData, String templateName)
    {
        renderOutput(engine,runData,templateName,DEFAULT_CONTENT_TYPE);
    }
    
    public static void renderOutput(TemplateEngine engine, RunData runData, String templateName, String contentType)
    {
        HttpServletResponse response = runData.getResponse();
        response.setContentType(contentType);
        Context ctx = runData.getContext();
        Writer writer = null;
        try
        {
            writer = response.getWriter();
            engine.merge(templateName,ctx,writer);
            writer.flush();
        }
        catch(Exception e)
        {
            logger.error("Error rendering layout ["+templateName+"]",e);
        }
        finally
        {
            if(writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (IOException e)
                {
                    logger.warn("Error closing output stream",e);                    
                }
            }
        }
    }

}
