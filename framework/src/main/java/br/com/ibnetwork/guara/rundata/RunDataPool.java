package br.com.ibnetwork.guara.rundata;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RunDataPool
{
    RunData createFrom(HttpServletRequest request, HttpServletResponse response, ServletConfig servletConfig)
    	throws Exception;
}
