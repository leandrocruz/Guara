package br.com.ibnetwork.guara.pull.tools;

import java.io.StringWriter;
import java.io.Writer;

import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;
import br.com.ibnetwork.xingu.template.TemplateEngine;
import br.com.ibnetwork.xingu.template.TemplateEngineException;
import br.com.ibnetwork.xingu.container.Inject;

/**
 * @author leandro
 */
public class TemplateTool
	extends ApplicationToolSupport
{
    @Inject
    private TemplateEngine engine;

    public String render(String templateName)
    	throws TemplateEngineException
    {
        Writer writer = new StringWriter();
        engine.merge(templateName,data.getContext(),writer);    
        return writer.toString();
    }
    
    public boolean templateExists(String templateName)
    	throws TemplateEngineException
    {
        return engine.templateExists(templateName);
    }
    
    public String getRawText(String templateName)
    	throws TemplateEngineException
    {
    	return engine.getRawText(templateName);
    }
}
