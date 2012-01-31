package br.com.ibnetwork.guara.metadata.impl;

import br.com.ibnetwork.guara.metadata.DatabaseMetadata;
import br.com.ibnetwork.guara.metadata.MetadataSupport;
import br.com.ibnetwork.xingu.utils.JdbcUtils;

public class HsqlDb
	extends MetadataSupport
	implements DatabaseMetadata
{	
	public void init()
		throws Exception
	{
		super.init();
		template = "/templates/orm/ddl/HsqlDb.ftl";
		outputDirectory = mainResource.getDirectory()+"/../../sql/hsqldb"; 
		outputFile = outputDirectory+"/"+getReferenceName()+".script";
	}

	public String getTableName()
    {
	    return JdbcUtils.toColumnName(getReferenceName());
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
