package br.com.ibnetwork.guara.pipeline.valve.view;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import br.com.ibnetwork.guara.pipeline.PipelineException;
import br.com.ibnetwork.guara.pipeline.valve.ValveSupport;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.view.TemplateUtils;
import xingu.container.Inject;
import xingu.template.TemplateEngine;

/**
 * @author leandro
 */
public class WriteOutput
    extends ValveSupport
    implements Configurable
{
    @Inject
    private TemplateEngine engine;

    private String defaultLayout = "layouts.ajax.Body";
    
    private String iFrameIO = "layouts.ajax.iFrameIO";
    
    public void configure(Configuration conf) 
        throws ConfigurationException 
    {
    	defaultLayout = conf.getChild("defaultLayout").getAttribute("template",defaultLayout);
    	iFrameIO = conf.getChild("defaultLayout").getAttribute("iFrameIO",iFrameIO);
    }

    public boolean execute(RunData data) 
    	throws PipelineException
    {
    	String layout = defaultLayout;
    	if(ServletFileUpload.isMultipartContent(data.getRequest()))
    	{
    		layout = iFrameIO;
    	}
    	TemplateUtils.renderOutput(engine,data,layout);
        return true;
    }
}
