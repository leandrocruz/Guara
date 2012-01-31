package br.com.ibnetwork.guara.metadata;

import java.io.File;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import br.com.ibnetwork.guara.MavenUtils;
import br.com.ibnetwork.guara.metadata.handler.FileHandler;
import br.com.ibnetwork.guara.metadata.handler.WriteStringToFile;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

public abstract class MetadataSupport
    implements Metadata
{
	protected MavenProject project;
	
	protected String domainClass;

	protected BeanInfo beanInfo;
	
	protected String shortDomainClass;
	
	protected String basePackage;
	
	protected String outputFile;
	
	protected String outputDirectory;
	
	protected String template;
	
	protected Resource mainResource;
	
	protected Resource mainTestResource;
	
	protected String modelName;
	
	protected FileHandler handler;
	
	@Inject
	private Factory factory;

	public void setProject(MavenProject project)
    {
		this.project = project;
		this.basePackage = project.getGroupId();
		this.mainResource = (Resource) project.getResources().get(0);
		this.mainTestResource = (Resource) project.getTestResources().get(0);
    }

	public void setModelName(String modelName)
    {
		this.modelName = modelName;
		this.domainClass = project.getGroupId()+"."+modelName;
		this.shortDomainClass = ClassUtils.getShortClassName(domainClass);
    }
	
	public String getModelName()
	{
		return modelName;
	}
	
	public void setFileHandler(FileHandler handler)
    {
		this.handler = handler;
    }

	public void init()
        throws Exception
    {}

	public BeanInfo getBeanInfo()
		throws Exception
	{
		if(beanInfo == null)
		{
			ClassLoader loader = MavenUtils.getProjectClassLoader(project);
			Object bean = ObjectUtils.getInstance(domainClass, loader);
			beanInfo = (BeanInfo) factory.create(BeanInfo.class, new Object[]{bean}, new String[]{Object.class.getName()});
		}
		return beanInfo;
	}

	public String getDomainClass()
	{
		return domainClass;
	}

	public String getOutputFile()
	{
		return outputFile;
	}

	public String getOutputDirectory()
	{
		return outputDirectory;
	}

	public MavenProject getProject()
	{
		return project;
	}

	public String getReferenceName()
	{
		return StringUtils.uncapitalize(shortDomainClass);
	}

	public String getShortDomainClass()
	{
		return shortDomainClass;
	}

	public String getTemplate()
    {
	    return template;
    }

//	public String getShortModelAsPath() 
//	{
//		return modelName.replace(".", "/").toLowerCase();
//	}

	public void saveResults(String text, boolean overwrite)
		throws Exception
	{
		File dir = new File(outputDirectory);
		if(!dir.exists())
		{
			log("creating directory: "+outputDirectory);
			dir.mkdirs();
		}
		
		File file = new File(outputFile);
		log("Saving results to: "+outputFile);
		if(overwrite || !file.exists() || overrideResults())
		{
			if(handler == null)
			{
				handler = WriteStringToFile.instance();
			}
			handler.handleFile(file, text, this);
		}
		else
		{
			log("** Can't override file: "+outputFile+" **");
		}
	}

	
	public boolean deleteFileOnClean()
    {
		return true;
    }

	protected void log(String message)
    {
		System.out.println("[LOG] "+getClass().getSimpleName()+" : "+message);
    }

	protected abstract boolean overrideResults();

}
