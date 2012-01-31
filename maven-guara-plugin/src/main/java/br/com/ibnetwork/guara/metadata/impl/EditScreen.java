package br.com.ibnetwork.guara.metadata.impl;

import br.com.ibnetwork.guara.metadata.JavaMetadataSupport;
import br.com.ibnetwork.guara.metadata.Metadata;

public class EditScreen
    extends JavaMetadataSupport
{
	public void init()
		throws Exception
	{
		super.init();
		template = "/templates/view/freemarker/Edit.ftl";
		outputDirectory = project.getBasedir()+"/"+Metadata.WEB_APP+"/templates/app/screens/admin/guara/" + getReferenceName();
		outputFile = outputDirectory+"/Edit.ftl";	       
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
