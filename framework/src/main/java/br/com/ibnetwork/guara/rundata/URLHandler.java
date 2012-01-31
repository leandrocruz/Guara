package br.com.ibnetwork.guara.rundata;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface URLHandler 
{
    Map<String, String> handleURL(HttpServletRequest request)
		throws Exception;
}
