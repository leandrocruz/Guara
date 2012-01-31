package br.com.ibnetwork.guara.metadata.impl;

import java.io.File;

import br.com.ibnetwork.guara.metadata.JavaMetadataSupport;

public class CrudTest
	extends JavaMetadataSupport
{
	protected String ref;

	public void init()
		throws Exception
	{
		super.init();
		packageName = project.getGroupId()+".om.OtherBean";
		baseDir = project.getBuild().getTestSourceDirectory();
		baseDir +="."+packageName;
		outputDirectory = baseDir.replaceAll("\\.", "/");
		outputDirectory = new File(outputDirectory).getCanonicalPath();
		compilationUnit += "CrudTest";
		outputFile = outputDirectory+File.separator+compilationUnit+".java";
		template = "/templates/java/guara/CrudTest.ftl";
		ref = shortDomainClass.toLowerCase();
	}

	@Override
    protected boolean overrideResults()
    {
	    return true;
    }

	@Override
    public boolean deleteFileOnClean()
    {
	    return true;
    }
	
	public String getRef() 
	{
		return ref;
	}
}
