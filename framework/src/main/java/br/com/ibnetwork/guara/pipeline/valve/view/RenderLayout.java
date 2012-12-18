package br.com.ibnetwork.guara.pipeline.valve.view;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.PageInfo;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.view.TemplateUtils;
import br.com.ibnetwork.xingu.container.Inject;
import xingu.template.Context;
import xingu.template.TemplateEngine;

/**
 * @author leandro
 */
public class RenderLayout
    extends ValveSupport
    implements Configurable
{
    private Log logger = LogFactory.getLog(RenderLayout.class);
    
    @Inject
    private TemplateEngine engine;
    
    private String onTemplateNotFound;

    private String defaultLayout;
    
    public void configure(Configuration conf)
    	throws ConfigurationException
    {
        onTemplateNotFound = "layouts." + conf.getChild("onLayoutNotFound").getAttribute("template","LayoutNotFound");
        defaultLayout = conf.getChild("defaultLayout").getAttribute("template","Default");
    }

    public boolean execute(RunData runData) 
    	throws PipelineException
    {
        PageInfo info = runData.getPageInfo();
        String layoutTemplateName = info.getLayoutTemplate();
        String templateName = info.getTemplate();
        
        //try to match screen template
        if(layoutTemplateName == null && templateName != null)
        {
            layoutTemplateName = searchLayoutForTemplate(templateName);
        }
        else if(layoutTemplateName == null)
        {
            layoutTemplateName = defaultLayout;
        }
        layoutTemplateName = "layouts." + layoutTemplateName;
        
        Context ctx = runData.getContext();
        if(!engine.templateExists(layoutTemplateName))
        {
            ctx.put("layoutTemplateName",layoutTemplateName);
            layoutTemplateName = onTemplateNotFound;
            logger.debug("Layout ["+layoutTemplateName+"] not found. Will render ["+onTemplateNotFound+"]");
        }
        TemplateUtils.renderOutput(engine,runData,layoutTemplateName);
        return true;
    }

    private String searchLayoutForTemplate(String templateName)
    {
        String realName = "layouts." + templateName;
        //System.out.println("Layout: " + realName);
        if(engine.templateExists(realName))
        {
            return templateName;
        }
        String[] parts = StringUtils.split(templateName,".");
        int last = parts.length - 1; 
        String lastName = parts[last];
        if(defaultLayout.equals(lastName))
        {
            if(last == 0)
            {
                //no template found. Sorry !
                return null;
            }
            // up one level
            String[] array = new String[last];
            System.arraycopy(parts,0,array,0,last--);
            parts = array;
        }
        parts[last] = defaultLayout;
        templateName = StringUtils.join(parts,".");
        return searchLayoutForTemplate(templateName);
    }
    
}
