package br.com.ibnetwork.guara.mojo.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import br.com.ibnetwork.guara.mojo.MojoSupport;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * @goal s3upload
 * @requiresProject true
 */
public class UploadToS3Mojo
	extends MojoSupport
{
	/**
	 * @parameter expression="${output}
	 */
	public static String output = "";

	/**
	 * @parameter expression="${compress}
	 */
	public static boolean compress = false;
	
	/**
	 * @parameter expression="${s3}
	 */
	public S3Config s3;

	
	@Override
	protected void go()
		throws Exception
	{
		String credentials = s3.getCredentials();
		String bucket = s3.getBucket();
		String endpoint = s3.getEndpoint();
		
		log.info("---------------------------------------------------------------");
		log.info("Root: " + output);
		log.info("Credentials: " + credentials);
		log.info("Buket: " + bucket);
		log.info("Endpoint: " + endpoint);
		log.info("---------------------------------------------------------------");
		
		
		File file = new File(credentials);
		if(!file.exists())
		{
			throw new Exception("Credentials file '" + credentials + "' missing");
		}
		InputStream is = new FileInputStream(file);
		AmazonS3 amazonS3 = new AmazonS3Client(new PropertiesCredentials(is));
		IOUtils.closeQuietly(is);
		
		File root = new File(output);
		if(!root.exists())
		{
			throw new Exception("Root Directory '" + root + "' is empty");
		}

		if(StringUtils.isNotEmpty(endpoint))
		{
			amazonS3.setEndpoint(endpoint);
		}
		
		new S3(amazonS3, compress).upload(root, bucket);
	}
}
