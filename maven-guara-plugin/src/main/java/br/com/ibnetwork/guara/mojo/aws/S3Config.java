package br.com.ibnetwork.guara.mojo.aws;


public class S3Config
{
	private String bucket;

	private String endpoint;

	private String credentials;

	public String getBucket()
	{
		return bucket;
	}

	public void setBucket(String bucket)
	{
		this.bucket = bucket;
	}

	public String getEndpoint()
	{
		return endpoint;
	}

	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}

	public String getCredentials()
	{
		return credentials;
	}

	public void setCredentials(String credentials)
	{
		this.credentials = credentials;
	}

	
}
