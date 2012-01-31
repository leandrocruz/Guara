package br.com.ibnetwork.guara.pipeline.valve.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.modules.ModuleLoader;
import br.com.ibnetwork.guara.modules.ModuleLoaderException;
import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.PageInfo;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.view.TemplateUtils;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.template.Context;
import br.com.ibnetwork.xingu.template.TemplateEngine;

public class RenderScreen
    extends ValveSupport
    implements Configurable
{
    private Log logger = LogFactory.getLog(RenderScreen.class);
    
    @Inject
    private TemplateEngine engine;
    
    @Inject
    private ModuleLoader moduleLoader;
    
    private String onTemplateNotFound;

    private String defaultTemplate;

    private OutcomeMapping[] mappings;
    
    private Map<String, String> aliasMap;
    
    public static final String SCREEN_CONTEXT_KEY = "screen_placeholder";
    
    public void configure(Configuration root)
    	throws ConfigurationException
    {
        onTemplateNotFound = "screens." + root.getChild("onTemplateNotFound").getAttribute("template","TemplateNotFound");
        defaultTemplate = root.getChild("defaultTemplate").getAttribute("template","Index");
        Configuration[] modules = root.getChild("templateMapping").getChildren("module");
        mappings = new OutcomeMapping[modules.length];
        for (int i = 0; i < modules.length; i++)
        {
            Configuration conf = modules[i];
            String name = conf.getAttribute("name");
            String type = conf.getAttribute("type",ModuleLoader.ACTION_TYPE);
            try
            {
	            Class<?> moduleClass = moduleLoader.loadModuleClass(name, type);
	            List<Configuration> outcomes = getMapping(conf);
	            mappings[i] = new OutcomeMapping(moduleClass,outcomes);
            }
            catch (ModuleLoaderException e)
            {
            	log.error("Can't load module: "+name,e);
            }
        }
        
        //alias
        Configuration[] aliases = root.getChildren("alias");
        aliasMap = new HashMap<String, String>(aliases.length);
        for (Configuration alias : aliases)
        {
            String name = alias.getAttribute("name");
            String target = alias.getAttribute("target");
            aliasMap.put(name, target);
        }
    }

    private List<Configuration> getMapping(Configuration root)
    {
        Configuration[] conf = root.getChildren("outcome");
        List<Configuration> result = new ArrayList<Configuration>();
        for (int j = 0; j < conf.length; j++)
        {
            Configuration outcome = conf[j];
            result.add(outcome);
        }
        return result;
    }

    public boolean execute(RunData data) 
    	throws PipelineException
    {
    	
    	String templateName = getTemplateName(data);
    	templateName = unalias(templateName);
    	if(templateName.endsWith("."))
    	{
    	    templateName = templateName.substring(0, templateName.length() - 1);
    	}
    	
    	/*
    	 * store template on page info so we have access to which template was rendered
    	 */
    	PageInfo pageInfo = data.getPageInfo(); 
    	pageInfo.setTemplate(templateName); 
        String toRender = "screens."+templateName; 

        Context ctx = data.getContext();
        if(!engine.templateExists(toRender))
        {
            String index = toRender + ".Index";
            if(!engine.templateExists(index))
            {
                ctx.put("templateName", templateName);
                toRender = onTemplateNotFound;
                logger.debug("Template ["+templateName+"] not found. Will render ["+onTemplateNotFound+"]");
            }
            else
            {
                pageInfo.setTemplate(templateName + ".Index"); 
                toRender = index;
            }
        }
        String result = TemplateUtils.renderTemplate(engine, data.getParameters().getEncoding(), data.getContext(), toRender);
        ctx.put(SCREEN_CONTEXT_KEY, result);
        return true;
    }

    private String unalias(String templateName)
    {
        String target = aliasMap.get(templateName);
        return target != null ? target : templateName;
    }

    private String getTemplateName(RunData data)
    {
        Outcome outcome = data.getOutcome();
        String templateName = data.getPageInfo().getTemplate();
        /*
         * Check template from RunData 1st 
         */
        if(templateName != null)
        {
            return templateName;
        }
        
        /*
         * If we have some outcome, try to apply the mapping 
         */
        else if(outcome != null && !outcome.equals(Outcome.UNKNOWN)) 
        {
            for (int i = 0; i < mappings.length; i++)
            {
                OutcomeMapping mapping = mappings[i];
                if(mapping.moduleClass.isAssignableFrom(outcome.getSource().getClass()))
                {
                	String tmp = mapping.getTemplateName(outcome);
                	if(StringUtils.isNotEmpty(tmp))
                	{
                        templateName = tmp; 
                	}
                }
            }
        }
        
        /*
         * Fall back 
         */
        if(templateName == null)
        {
            log.warn("Can't determine template for outcome: "+outcome);
            templateName = defaultTemplate;
        }
        return templateName;
    }
}

class OutcomeMapping
{
    List<Configuration> outcomes;
    
    Class<?> moduleClass;

    public OutcomeMapping(Class<?> moduleClass, List<Configuration> outcomes)
    {
        this.moduleClass = moduleClass;
        this.outcomes = outcomes;
    }

    public String getTemplateName(Outcome outcome)
    {
    	String defaultTemplate = null;
        for (Configuration conf : outcomes) 
        {
			String template = conf.getAttribute("template", null);
			String code = conf.getAttribute("code","DEFAULT");
            String method = conf.getAttribute("method",null);
            if(code.equals(outcome.getCode()) 
                    && (method == null ? outcome.getMethod() == null : method.equals(outcome.getMethod())))
            {
                return template;
            }
            else if("DEFAULT".equals(code))
			{
				defaultTemplate = template;
			}
		}
        return defaultTemplate;
    }
}