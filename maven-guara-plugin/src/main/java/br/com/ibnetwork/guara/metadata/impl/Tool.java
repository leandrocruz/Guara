package br.com.ibnetwork.guara.metadata.impl;

import java.io.File;

import br.com.ibnetwork.guara.metadata.JavaMetadataSupport;

public class Tool
	extends JavaMetadataSupport    
{
	public void init()
		throws Exception
	{
		super.init();
		packageName = project.getGroupId()+".xingu.pull";
		baseDir +="."+packageName;
		outputDirectory = baseDir.replaceAll("\\.", "/");
		outputDirectory = new File(outputDirectory).getCanonicalPath();
		outputFile = outputDirectory+File.separator+shortDomainClass+"Tool.java";
		template = "/templates/java/guara/Tool.ftl";	
		compilationUnit += "Tool";
	}

	@Override
    protected boolean overrideResults()
    {
	    return false;
    }
}
