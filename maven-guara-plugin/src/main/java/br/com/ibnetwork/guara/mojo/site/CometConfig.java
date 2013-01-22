package br.com.ibnetwork.guara.mojo.site;


public class CometConfig
{
	private String host;
	
	private int port = 80;

	public CometConfig()
	{
		System.out.println("");
	}
	
	public String getHost() {return host;}
	public void setHost(String host)
	{
		this.host = host;
	}
	public int getPort() {return port;}
	public void setPort(int port)
	{
		this.port = port;
	}
}
