package br.com.ibnetwork.guara.mojo.site;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.MethodUtils;

public class RunGuara
{
	public String execute(String pulga, ClassLoader cl)
		throws Exception
	{
		String location = cl.getResource(pulga).getFile();
		Object guara = cl.loadClass("br.com.ibnetwork.guara.Guara").newInstance();

		/* Guara.init() */
		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(config.getInitParameter("pulgaConfiguration")).thenReturn(pulga);
		when(context.getRealPath(pulga)).thenReturn(location);
		//guara.init(config);
		MethodUtils.invokeMethod(guara, "init", config);

		/* Fake request */
		@SuppressWarnings("rawtypes")
		Enumeration paramNames = mock(Enumeration.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameterNames()).thenReturn(paramNames);
		when(request.getPathInfo()).thenReturn("");
		when(request.getServletPath()).thenReturn("");

		/* Fake Session */
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);

		SimpleResponse response = new SimpleResponse();
		//guara.doGet(request, response);
		MethodUtils.invokeMethod(guara, "doGet", new Object[]{request, response});

		String result = response.getResponse();
		return result;
	}
}
