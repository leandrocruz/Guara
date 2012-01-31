package br.com.ibnetwork.guara.metadata.impl;

import java.io.File;

import br.com.ibnetwork.guara.metadata.ControllerMetadata;

public class EmptyController
	extends ControllerMetadata
{
	public void init()
		throws Exception
	{
		super.init();
		template = "/templates/java/guara/EmptyController.ftl";
		compilationUnit += "Control";
		baseClass = shortDomainClass+"ControlSupport";
		outputFile = outputDirectory+File.separator+compilationUnit+".java";
	}

	@Override
    protected boolean overrideResults()
    {
	    return false;
    }


}
