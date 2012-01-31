package br.com.ibnetwork.guara.rundata.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.com.ibnetwork.guara.pull.tools.LinkTool;
import br.com.ibnetwork.guara.rundata.PageInfo;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.rundata.UrlRedirector;
import br.com.ibnetwork.guara.util.ServletUtils;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class UrlRedirectorImpl
    implements UrlRedirector
{

    public String locationFor(RunData data)
    {
        HttpServletRequest request = data.getRequest();
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();
        /* 
         * Must join servlet and path info because servlet mappping on web.xml may break things
         * http://bluxte.net/blog/2006-03/29-40-33.html 
         */
        String path = pathInfo != null ? pathInfo : StringUtils.EMPTY 
                + servletPath != null ? servletPath : StringUtils.EMPTY;
        
        if(path.startsWith(PageInfo.TEMPLATE) || path.startsWith(PageInfo.TEMPLATE, 1)
                || path.startsWith(PageInfo.ACTION) || path.startsWith(PageInfo.ACTION, 1)
                || path.startsWith(PageInfo.SCREEN) || path.startsWith(PageInfo.SCREEN, 1)
                || path.startsWith(PageInfo.LAYOUT) || path.startsWith(PageInfo.LAYOUT, 1))
        {
            //TODO: create a new URL from request
            Map<String, String> map = null;
            try
            {
                map = ServletUtils.pathToMap(path);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
            LinkTool tool = new LinkTool();
            tool.refresh(data);
            tool.setTemplate(map.get(PageInfo.TEMPLATE))
                .setPipeline(map.get(PageInfo.PIPELINE))
                .setAction(map.get(PageInfo.ACTION))
                .setScreen(map.get(PageInfo.SCREEN))
                .setLayout(map.get(PageInfo.LAYOUT))
                .addQueryString(request.getQueryString());
            
            return tool.toString();
        }
        else
        {
            return null;
        }

    }
}
