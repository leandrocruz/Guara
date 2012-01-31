package br.com.ibnetwork.guara.pull.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.guara.pipeline.valve.modules.ActionExecutor;
import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;
import br.com.ibnetwork.guara.rundata.PageInfo;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.util.ServletUtils;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class LinkTool
	extends ApplicationToolSupport
	implements Configurable
{
    private String baseAddress;
    
    private String pipeline;
    
    private String template;
    
    private String action;
    
    private String layout;
    
    private String screen;
    
    private String encoding = "UTF-8";
    
    private StringBuffer queryString = new StringBuffer();

    public LinkTool()
    {}

    public void configure(Configuration conf)
    	throws ConfigurationException
    {
        encoding = conf.getChild("encoding").getAttribute("name", "UTF-8");
    }

    public void refresh(RunData data)
    {
        this.data = data;
        clear();
        if(baseAddress != null)
        {
            return;
        }
        HttpServletRequest request = data.getRequest();
        baseAddress = ServletUtils.getBaseAddressFromRequest(request);
    }

    public LinkTool page(String templateName)
    {
        return setPage(templateName);
    }
    
    public LinkTool setPage(String templateName)
    {
        return setTemplate(templateName);
    }

    public LinkTool template(String templateName) 
    {
        return setTemplate(templateName);
    }
    
    public LinkTool setTemplate(String templateName)
    {
        this.template = StringUtils.trimToNull(templateName);
        return this;
    }

    public LinkTool pipeline(String pipelineName)
    {
        return setPipeline(pipelineName);
    }
    
    public LinkTool setPipeline(String pipelineName)
    {
    	this.pipeline = StringUtils.trimToNull(pipelineName);
        return this;
    }

    public LinkTool action(String actionName)
    {
        return setAction(actionName);
    }
    
    public LinkTool setAction(String actionName)
    {
        this.action = StringUtils.trimToNull(actionName);
        return this;
    }
    
    public LinkTool layout(String layoutName)
    {
        return setLayout(layoutName);
    }
    
    public LinkTool setLayout(String layoutName)
    {
        this.layout = StringUtils.trimToNull(layoutName);
        return this;
    }

    public LinkTool screen(String screenName)
    {
        return setScreen(screenName);
    }
    
    public LinkTool setScreen(String screenName)
    {
        this.screen = StringUtils.trimToNull(screenName);
        return this;
    }

    public LinkTool exec(String name)
        throws UnsupportedEncodingException
    {
    	name = StringUtils.trimToNull(name);
    	if(name == null)
    	{
    		return this;
    	}
        return addQueryData(ActionExecutor.METHOD_KEY+name,"1");
    }
    
    public LinkTool add(String name)
    {
        return addQueryData(name);
    }
    
    public LinkTool add(String name, Object value)
    {
        return addQueryData(name,value);
    }
    
    public LinkTool addQueryData(String name) 
    {
    	return addQueryData(name,null);
    }

    public LinkTool addQueryData(String name, Object value) 
    {
        if(name == null)
        {
            return this;
        }
        name = encode(name);
        appendToQueryString(name);
        if(value != null)
        {
        	value = encode(value.toString());
        	queryString.append(StringUtils.EQUALS).append(value);
        }
        return this;
    }
    
    public LinkTool addQueryString(String query)
    {
        appendToQueryString(query);
        return this;
    }

    private void appendToQueryString(String name)
    {
        if(StringUtils.isEmpty(name))
        {
            return;
        }
        
        if(queryString.length() == 0)
        {
            queryString.append(StringUtils.QUESTION_MARK);
        }
        else
        {
            queryString.append(StringUtils.AND);
        }
        queryString.append(name);
    }

    
    private String encode(String s)
    {
        try
        {
            return URLEncoder.encode(s, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(baseAddress);
        if(StringUtils.isNotEmpty(template))
        {
            if(template.indexOf(StringUtils.DOT) >= 0)
            {
                template = template.replaceAll("\\.", StringUtils.SLASH);
            }
            sb.append(StringUtils.SLASH).append(template);
        }
        addIfValueNotNull(PageInfo.PIPELINE, pipeline);
        addIfValueNotNull(PageInfo.ACTION, action);
        addIfValueNotNull(PageInfo.SCREEN, screen);
        addIfValueNotNull(PageInfo.LAYOUT, layout);
        
        if(queryString.length() > 0)
        {
            sb.append(queryString);
        }
        clear();
        String result = sb.toString();
        return result; 
    }

    public LinkTool addIfValueNotNull(String name, String value)
    {
        if(StringUtils.isNotEmpty(value))
        {
            addQueryData(name, value);
        }
        return this;
    }

    private void clear()
    {
        pipeline = null;
        template = null;
        screen = null;
        layout = null;
        action = null;
        if(queryString.length() > 0)
        {
            queryString = new StringBuffer();
        }
    }
}
