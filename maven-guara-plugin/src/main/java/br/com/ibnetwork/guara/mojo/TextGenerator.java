package br.com.ibnetwork.guara.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import br.com.ibnetwork.guara.MavenUtils;
import br.com.ibnetwork.guara.generator.Generator;
import br.com.ibnetwork.guara.generator.XinguTextGenerator;
import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.handler.FileHandler;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.factory.Factory;

public abstract class TextGenerator
	extends OldMojoSupport
{
	private Generator generator;
	
	private Factory factory;

	protected void generate(Metadata meta) 
		throws Exception
    {
        String text = generator.generate(meta);
        meta.saveResults(text,overwrite);
    }

	protected void init() 
		throws Exception
    {
    	Container pulga = ContainerUtils.getContainer();
    	factory = (Factory) pulga.lookup(Factory.class);
    	generator = (Generator) factory.create(XinguTextGenerator.class);
    }

	public void execute() 
		throws MojoExecutionException, MojoFailureException
    {
		//1. print environment
		printEnv();
		
		model = StringUtils.trimToNull(model);
		
        //2. check user input
    	if(model == null && (modelNames == null || modelNames.size() == 0))
    	{
    		throw new MojoExecutionException(
    				"Please, use the -Dmodel switch to provide the model for code generation. Example: om.MyPojo" +
    				"\nOR\n" +
    				"Configure the maven-guara-plugin in your pom.xml");
    	}

    	//3. create pulga singleton
    	try
        {
	        init();
        }
        catch (Exception e)
        {
        	throw new MojoExecutionException("Error initializing pulga",e);
        }
        
    	//4. generate
    	List<String> expandedModelNames = expandModelNames();
    	for (String modelName : expandedModelNames)
        {
    		executeMetaForModel(modelName);    
        }
    	
    	//5. terminate
    	try
        {
    		terminate();
        }
        catch (Exception e)
        {
        	throw new MojoExecutionException("Error terminating",e);
        }
    }

	protected void terminate()
		throws Exception
    {
		//do nothing by default
    }

	private List<String> expandModelNames()
    {
		List<String> result = new ArrayList<String>();
	    if(model != null)
    	{
    		result.add(model);
    	}
    	else
    	{  	
    		String rootName = new File(project.getBuild().getSourceDirectory()+"/"+project.getGroupId().replaceAll("\\.", "/")).getAbsolutePath();
    		for (String name : modelNames) 
    		{	
				if(name.endsWith("*"))
				{
					addPojosInDirectory(result, rootName, name);
				}
				else
				{
					result.add(name);
				}
			}
    	}
	    return result;
    }

	private void addPojosInDirectory(List<String> list, String rootName, String name) 
	{
		String fullName = rootName+"."+ name.subSequence(0, name.length()-2);
		fullName = fullName.replaceAll("\\.", "/");
		File in = new File(fullName);
		getLog().info("--> add pojos in directory: " + in.getAbsolutePath());
		File[] pojos = in.listFiles();
		for (File pojo : pojos) 
		{
			String pojoName = pojo.getAbsolutePath().substring(rootName.length());
			if(pojoName.endsWith(".java"))
			{
				pojoName = pojoName.substring(1, pojoName.length()-5);
				getLog().info("--> add pojo: " + pojoName);
				pojoName = pojoName.replaceAll("/", ".");
				pojoName = pojoName.replaceAll("\\\\", ".");
				list.add(pojoName);
			}
		}
	}

	private void executeMetaForModel(String modelName)
        throws MojoExecutionException
    {
	    String domainClass = project.getGroupId()+"."+modelName;
	    getLog().info("");
	    getLog().info("Running generator for: "+domainClass);
    	getLog().info("Overwrite: "+overwrite);
    	getLog().info("");
    	try
        {
    		ClassLoader loader = MavenUtils.getProjectClassLoader(project);
	        loader.loadClass(domainClass);
        }
        catch (ClassNotFoundException e)
        {
        	throw new MojoExecutionException("Can't find class: "+modelName,e);
        }
        catch (Exception e)
        {
        	throw new MojoExecutionException("Error initializing Mojo",e);
        }
        Metadata[] tasks = getTasks();
    	Metadata meta;
        for (Metadata task : tasks) 
        {
            meta = task;
            try 
            {
            	meta.setProject(project);
            	meta.setModelName(modelName);
            	meta.init();
                executeMeta(meta);
            }
            catch (Throwable t) 
            {
                //log and continue
                throw new MojoExecutionException("Error executing meta: " + t.getMessage(), t);
            }
        }
    }

	protected Metadata create(Class clazz)
	{
		return create(clazz, null);
	}
	
	protected Metadata create(Class clazz, FileHandler handler)
	{
		Metadata meta;
		meta = (Metadata) factory.create(clazz);
		meta.setFileHandler(handler);
		return meta;
	}
	
	protected abstract void executeMeta(Metadata meta)
		throws Exception;

	protected abstract Metadata[] getTasks();
}
