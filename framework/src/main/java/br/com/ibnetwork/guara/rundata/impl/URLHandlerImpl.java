package br.com.ibnetwork.guara.rundata.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.com.ibnetwork.guara.rundata.PageInfo;
import br.com.ibnetwork.guara.rundata.URLHandler;
import xingu.utils.StringUtils;

public class URLHandlerImpl 
	implements URLHandler
{
    public Map<String, String> handleURL(HttpServletRequest request)
        throws Exception
    {
        /* 
         * Must join servlet and path info because servlet mappping on web.xml may break things
         * http://bluxte.net/blog/2006-03/29-40-33.html 
         */
        String pathInfo = request.getPathInfo();
        if(StringUtils.isEmpty(pathInfo))
        {
            return null;
        }
        String path = /* StringUtils.orEmpty(request.getServletPath()) + */ StringUtils.orEmpty(pathInfo);
        String template = path.substring(1);
        if(template.startsWith(PageInfo.TEMPLATE))
        {
            template = template.substring(PageInfo.TEMPLATE.length() + 1);
        }
        template = template.replaceAll(StringUtils.SLASH, StringUtils.DOT);
        Map<String, String> result = new HashMap<String, String>();
        result.put(PageInfo.TEMPLATE, template);
        return result;
    }
}
