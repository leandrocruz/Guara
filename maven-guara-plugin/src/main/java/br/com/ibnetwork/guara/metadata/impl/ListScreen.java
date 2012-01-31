package br.com.ibnetwork.guara.metadata.impl;

import br.com.ibnetwork.guara.metadata.JavaMetadataSupport;

public class ListScreen
    extends JavaMetadataSupport
{
	public void init()
		throws Exception
	{
		super.init();
		template = "/templates/view/freemarker/List.ftl";
		outputDirectory = project.getBasedir()+"/WebContent/templates/app/screens/admin/guara/" + getReferenceName();
		outputFile = outputDirectory+"/List.ftl";	  
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
