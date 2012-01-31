package br.com.ibnetwork.guara.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtils
{
    public static Cookie getCookie(HttpServletRequest request, String name)
    {
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
        {
            return null;
        }
        for (int i = 0; i < cookies.length; i++)
        {
            Cookie cookie = cookies[i];
            if(cookie.getName().equalsIgnoreCase(name))
            {
                return cookie;
            }
        }
        return null;
    }
}
