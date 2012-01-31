package br.com.ibnetwork.guara.metadata;

import java.io.File;

public abstract class ControllerMetadata
	extends JavaMetadataSupport    
{
	public void init()
		throws Exception
    {
		super.init();
		packageName = project.getGroupId()+".guara.modules.actions";
		baseDir +="."+packageName;
		outputDirectory = baseDir.replaceAll("\\.", "/");
		outputDirectory = new File(outputDirectory).getCanonicalPath();
    }
}
