package br.com.ibnetwork.guara.rundata.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.com.ibnetwork.guara.rundata.URLHandler;
import br.com.ibnetwork.guara.util.ServletUtils;
import xingu.utils.StringUtils;

public class PathBasedURLHandler 
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
        String servletPath = request.getServletPath();
        String path = StringUtils.orEmpty(pathInfo) + StringUtils.orEmpty(servletPath);
        Map<String, String> map = ServletUtils.pathToMap(path);
        return map;
    }
}
