package br.com.ibnetwork.guara.metadata.impl;

import java.io.File;

import br.com.ibnetwork.guara.metadata.JavaMetadataSupport;

public class Bean
	extends JavaMetadataSupport    
{
	public void init()
		throws Exception
	{
		super.init();
		packageName = project.getGroupId()+".om";
		baseDir +="."+packageName;
		outputDirectory = baseDir.replaceAll("\\.", "/");
		outputFile = outputDirectory+File.separator+compilationUnit+".java";
		template = "/templates/java/guara/Bean.ftl";	
	}

	@Override
    protected boolean overrideResults()
    {
	    return false;
    }
}
