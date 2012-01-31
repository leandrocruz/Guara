package br.com.ibnetwork.guara.metadata.impl;

import java.io.File;

import br.com.ibnetwork.guara.metadata.ControllerMetadata;

public class ControllerSupport
    extends ControllerMetadata
{
	public void init()
		throws Exception
    {
		super.init();
		template = "/templates/java/guara/ControllerSupport.ftl";
		compilationUnit += "ControlSupport";
		outputFile = outputDirectory+File.separator+compilationUnit+".java";
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
}
