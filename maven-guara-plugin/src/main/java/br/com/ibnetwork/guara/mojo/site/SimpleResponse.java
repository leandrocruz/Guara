package br.com.ibnetwork.guara.mojo.site;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class SimpleResponse
	implements HttpServletResponse
{
	private StringWriter sw = new StringWriter();
	
	private PrintWriter writer = new PrintWriter(sw);

	private String charset;

	private String contentType;
	
	@Override
	public ServletOutputStream getOutputStream()
		throws IOException
	{
		throw new NotImplementedYet();
	}

	@Override
	public PrintWriter getWriter()
		throws IOException
	{
		return writer;
	}

	@Override
	public void setContentLength(int len)
	{}

	@Override
	public void setBufferSize(int size)
	{
		throw new NotImplementedYet();
	}

	@Override
	public int getBufferSize()
	{
		throw new NotImplementedYet();
	}

	@Override
	public void flushBuffer()
		throws IOException
	{
		throw new NotImplementedYet();
	}

	@Override
	public void resetBuffer()
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean isCommitted()
	{
		throw new NotImplementedYet();
	}

	@Override
	public void reset()
	{
		throw new NotImplementedYet();
	}

	@Override
	public void setLocale(Locale loc)
	{
		throw new NotImplementedYet();
	}

	@Override
	public Locale getLocale()
	{
		throw new NotImplementedYet();
	}

	@Override
	public void addCookie(Cookie cookie)
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean containsHeader(String name)
	{
		throw new NotImplementedYet();
	}

	@Override
	public String encodeURL(String url)
	{
		throw new NotImplementedYet();
	}

	@Override
	public String encodeRedirectURL(String url)
	{
		throw new NotImplementedYet();
	}

	@Override
	public String encodeUrl(String url)
	{
		throw new NotImplementedYet();
	}

	@Override
	public String encodeRedirectUrl(String url)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void sendError(int sc, String msg)
		throws IOException
	{
		throw new NotImplementedYet();
	}

	@Override
	public void sendError(int sc)
		throws IOException
	{
		throw new NotImplementedYet();
	}

	@Override
	public void sendRedirect(String location)
		throws IOException
	{
		throw new NotImplementedYet();
	}

	@Override
	public void setDateHeader(String name, long date)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void addDateHeader(String name, long date)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void setHeader(String name, String value)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void addHeader(String name, String value)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void setIntHeader(String name, int value)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void addIntHeader(String name, int value)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void setStatus(int sc)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void setStatus(int sc, String sm)
	{
		throw new NotImplementedYet();
	}
	
	public String getResponse()
	{
		return sw.toString();
	}

	@Override
	public String getContentType()
	{
		return this.contentType;
	}

	@Override
	public void setContentType(String type)
	{
		this.contentType = type;
	}

	@Override
	public void setCharacterEncoding(String charset)
	{
		this.charset = charset;
	}

	@Override
	public String getCharacterEncoding()
	{
		return charset;
	}
}
