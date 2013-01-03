package br.com.ibnetwork.guara.mojo.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import br.com.ibnetwork.xingu.utils.io.FileVisitor;
import br.com.ibnetwork.xingu.utils.io.TreeVisitor;
import br.com.ibnetwork.xingu.utils.io.zip.ZipUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.BinaryUtils;
import com.amazonaws.util.Md5Utils;

public class S3
	implements ProgressListener
{
	private final AmazonS3 s3;
	
	private final boolean compress;

	public S3(AmazonS3 s3, boolean compress)
	{
		this.s3 = s3;
		this.compress = compress;
	}

	public void upload(final File root, final String bucketName)
		throws Exception
	{
		new TreeVisitor(root, new FileVisitor()
		{
			@Override
			public void visit(File file)
				throws Exception
			{
				boolean isDirectory = file.isDirectory();
				if(!isDirectory)
				{
					String parent = file.getAbsolutePath();
					int len = root.getAbsolutePath().length();
					String key = parent.substring(len + 1);
					if(compress)
					{
						compress(file);
					}
					upload(bucketName, key, file);
				}
			}

		}).visit();
	}

	private void compress(File file)
		throws Exception
	{
		File gz = ZipUtils.gzip(file);
		file.delete();
		FileUtils.moveFile(gz, file);
	}

	private ObjectMetadata metaFor(File file)
		throws Exception
	{
		ObjectMetadata meta = new ObjectMetadata();
		InputStream is = new FileInputStream(file);
		String type = Mimetypes.getInstance().getMimetype(file);
        byte[] md5Hash = Md5Utils.computeMD5Hash(is);
		
		meta.setContentLength(file.length());
		meta.setContentMD5(BinaryUtils.toBase64(md5Hash));
		meta.setContentType(type);
		
		String name = file.getName();
		if(name.indexOf(".cache.") > 0) /* This name was generated @ GenerateStaticSiteMojo */
		{
			meta.setCacheControl("max-age=31536000, public"); // 60 * 60 * 24 * 365 = 1 year
		}
		else
		{
			meta.setCacheControl("no-cache");
		}

		if(compress)
		{
			meta.setContentEncoding("gzip");
		}
		
		return meta;
	}

	public void upload(String bucketName, String key, File file)
		throws Exception
	{
		ObjectMetadata meta = metaFor(file);
		InputStream is = new FileInputStream(file);
		PutObjectRequest put = new PutObjectRequest(bucketName, key, is, meta);
		put.setProgressListener(this);
		System.out.print("Uploading '" + file.getAbsolutePath() + "' to '"+ key + "' ");
		PutObjectResult result = s3.putObject(put);
		IOUtils.closeQuietly(is);
	}

	@Override
	public void progressChanged(ProgressEvent evt)
	{
		int code = evt.getEventCode();
		switch (code)
		{
			case ProgressEvent.COMPLETED_EVENT_CODE:
				System.out.println(" OK");
				break;

			case ProgressEvent.FAILED_EVENT_CODE:
				System.out.println(" ERR");
				break;

			default:
				break;
		}
	}
}