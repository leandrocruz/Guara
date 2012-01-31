package br.com.ibnetwork.guara.metadata.impl;

import br.com.ibnetwork.guara.metadata.JavaMetadataSupport;
import br.com.ibnetwork.guara.metadata.Metadata;

public class FreemarkerBeanMacro
    extends JavaMetadataSupport
{
	public void init()
    {
		template = "/templates/view/freemarker/Macro.ftl";
		outputDirectory = project.getBasedir()+"/"+Metadata.WEB_APP+"/templates/app/lib";
		outputFile = outputDirectory+"/GuaraGeneratedMacros.ftl";
    }

	@Override
    protected boolean overrideResults()
    {
	    return false;
    }

	@Override
    public boolean deleteFileOnClean()
    {
	    return true;
    }

}
