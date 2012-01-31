package br.com.ibnetwork.guara;

import java.io.File;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import br.com.ibnetwork.guara.generator.Generator;
import br.com.ibnetwork.guara.generator.XinguTextGenerator;
import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.handler.FileHandler;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.FSUtils;

public class GeneratorTestSupport
    extends XinguTestCase
{
	@Inject Factory factory;
	
	static final String BASE_DIR = "target";

	protected Metadata createMeta(Class clazz)
    {
    	return createMeta(clazz, "om.OtherBean");
    }

	protected Metadata createMeta(Class clazz, String model)
    {
    	return createMeta(clazz, model, null);
    }

	protected Metadata createMeta(Class clazz, String model, FileHandler handler)
    {
    	MavenProject project = createSampleProject();
    	Metadata meta= (Metadata) factory.create(clazz);
    	meta.setProject(project);
    	meta.setModelName(model);
    	meta.setFileHandler(handler);
    	try
        {
	        meta.init();
        }
        catch (Exception e)
        {
	        e.printStackTrace();
        }
    	return meta;
    }

	private MavenProject createSampleProject()
    {
    	
    	Model model = new Model();
    	model.setGroupId("br.com.ibnetwork.guara.myapp");
    	
    	Build build = new Build();
    	build.setSourceDirectory(BASE_DIR+"/src/main/java");
    	build.setTestSourceDirectory(BASE_DIR+"/src/test/java");
    	model.setBuild(build);
    
    	Resource resource = new Resource();
    	resource.setDirectory(BASE_DIR+"/src/main/resources");
    	build.addResource(resource);
    
    	Resource testResource = new Resource();
    	testResource.setDirectory(BASE_DIR+"/src/test/resources");
    	build.addTestResource(testResource);
    	
    	MavenProject project = new MavenProject(model);
    	
    	String pom = FSUtils.loadFromClasspath("myPom.xml");
    	project.setFile(new File(pom));
    
    	return project;
    }

	protected void callGenerator(Metadata meta)
    {
    	try
    	{
    		Generator generator = (Generator) factory.create(XinguTextGenerator.class);
    		String result = generator.generate(meta);
    		//System.out.println("******\n" + result + "\n******\n");
    		meta.saveResults(result,true);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
