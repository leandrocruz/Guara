package br.com.ibnetwork.guara.mojo.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

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
	 * @parameter expression="${bucket}
	 */
	public static String bucket = "";

	/**
	 * @parameter expression="${credentials}
	 */
	public static String credentials = "";
	
	@Override
	protected void go()
		throws Exception
	{
		log.info("---------------------------------------------------------------");
		log.info("Root: " + output);
		log.info("Credentials: " + credentials);
		log.info("Buket: " + bucket);
		log.info("---------------------------------------------------------------");
		
		File file = new File(credentials);
		if(!file.exists())
		{
			throw new Exception("Credentials file '" + credentials + "' missing");
		}
		InputStream is = new FileInputStream(file);
		AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(is));
		IOUtils.closeQuietly(is);
		
		File root = new File(output);
		if(!root.exists())
		{
			throw new Exception("Root Directory '" + root + "' is empty");
		}
		
		new S3(s3).upload(root, bucket);
	}
}
