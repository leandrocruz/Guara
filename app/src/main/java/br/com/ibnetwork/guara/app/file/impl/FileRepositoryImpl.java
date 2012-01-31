package br.com.ibnetwork.guara.app.file.impl;

import java.io.File;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.app.file.FileRepository;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.Environment;
import xingu.store.PersistentBean;

public class FileRepositoryImpl
    implements FileRepository, Configurable
{
	private Log log = LogFactory.getLog(FileRepositoryImpl.class);
	
	private File root;

	private String basePackage;
	
	@Inject
    protected Environment env;
	
	public void configure(Configuration conf)
    	throws ConfigurationException
    {
		String rootDir = conf.getChild("root").getAttribute("dir","${app.root}");
		basePackage = conf.getChild("basePackage").getAttribute("name","br.com.ibnetwork");
		rootDir = env.replaceVars(rootDir);
		root = new File(rootDir);
		if(!root.exists())
		{
			root.mkdirs();
		}
    }

	public File getRootDir()
	{
		return root;
	}

	public String getTargetDir(Object bean, String dirName)
	{
		if(bean == null)
		{
			return null;
		}
		String className = bean.getClass().getName();
		String relative = className.substring(basePackage.length());
		String id = null;
		if(bean instanceof PersistentBean)
		{
			id = String.valueOf(((PersistentBean) bean).getId());
		}
		relative = FileRepository.UGLY + relative.replaceAll("\\.", "/").toLowerCase()
			+(id != null ? "/"+id : "")
			+(dirName != null ? "/"+dirName : "");
		return relative;
	}

	public File store(Object bean, FileItem fi, String dirName, String fileName)
    	throws Exception
    {
		return store(bean, fi, dirName, fileName, true, false);
    }

	public File store(Object bean, FileItem fi, String dirName, String fileName, boolean overwrite, boolean deleteOnExit) 
		throws Exception
    {
		if(StringUtils.isEmpty(fileName) || fi.getSize() <= 0)
		{
			return null;
		}

		String path = getTargetDir(bean, dirName);

		//check relative dir
		File baseDir = new File(root, path);
		if(!baseDir.exists())
		{
			baseDir.mkdirs();
		}
		File file = new File(baseDir,fileName);
		log.info("Writing file to: "+ file+". Overwrite: "+overwrite);
		
		if(file.exists() && !overwrite)
		{
			return null;
		}
		else
		{
			fi.write(file);
			if(deleteOnExit)
			{
				file.deleteOnExit();
			}
			return file;
		}
    }
}
