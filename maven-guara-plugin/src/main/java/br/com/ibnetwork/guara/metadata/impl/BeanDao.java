package br.com.ibnetwork.guara.metadata.impl;

import java.io.File;

import br.com.ibnetwork.guara.metadata.JavaMetadataSupport;

public class BeanDao
	extends JavaMetadataSupport    
{
	public void init()
		throws Exception
	{
		super.init();
		packageName = project.getGroupId()+".om.dao";
		baseDir +="."+packageName;
		outputDirectory = baseDir.replaceAll("\\.", "/");
		outputDirectory = new File(outputDirectory).getCanonicalPath();
		compilationUnit += "Dao";
		outputFile = outputDirectory+File.separator+compilationUnit+".java";
		template = "/templates/java/guara/BeanDao.ftl";
	}

	@Override
    protected boolean overrideResults()
    {
	    return false;
    }
}
