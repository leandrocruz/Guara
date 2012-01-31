package br.com.ibnetwork.guara.util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import br.com.ibnetwork.xingu.utils.StringUtils;

public class ServletUtils
{
    public static final String getBaseAddressFromRequest(HttpServletRequest request)
    {
        String protocol = request.getScheme();
        String hostName = request.getServerName();
        int portNumber = request.getServerPort();
        String contextPath = request.getContextPath();
        
        String baseAddress = protocol
        	+ "://"
        	+ hostName
        	+ (portNumber == 80 ? StringUtils.EMPTY : ":" + portNumber)
        	+ contextPath;
        return baseAddress;
    }
    
    public static String getURLFromRequest(HttpServletRequest request)
    {
    	/* DEBUG 
    	System.out.println("Path info: "+request.getPathInfo());
        System.out.println("Context path: "+request.getContextPath());
        System.out.println("Servlet path: "+request.getServletPath());
    	 */
    	
    	String baseAddress = getBaseAddressFromRequest(request);
        //System.out.println("Base Address: "+baseAddress);
    	String query = request.getQueryString();
    	String pathInfo = request.getPathInfo();
    	String servletPath = request.getServletPath();
    	String path = StringUtils.orEmpty(pathInfo) + StringUtils.orEmpty(servletPath);
        String url = baseAddress 
            + (StringUtils.isNotEmpty(path) ? StringUtils.SLASH + path : StringUtils.EMPTY)
            + (query != null ? StringUtils.QUESTION_MARK + query : StringUtils.EMPTY); 
        //System.out.println("URL: "+url);
        //System.out.println("--");
        return url;
    }
    
    public static Map<String, String> pathToMap(String path)
        throws Exception
    {
        return pathToMap(path, "UTF-8");
    }
    
    public static Map<String, String> pathToMap(String path, String encoding)
        throws Exception
    {
        Map<String, String> result = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(path, StringUtils.SLASH);
        boolean isName = true;
        String value = null;
        String name = null;
        while (st.hasMoreTokens())
        {
            String token = st.nextToken();
            if (isName)
            {
                name = URLDecoder.decode(token, encoding);
                isName = false;
            }
            else
            {
                value = URLDecoder.decode(token, encoding);
                if(name != null && name.length() > 0)
                {
                    result.put(name, value);
                }
                isName = true;
            }
        }
        return result;
    }
}
