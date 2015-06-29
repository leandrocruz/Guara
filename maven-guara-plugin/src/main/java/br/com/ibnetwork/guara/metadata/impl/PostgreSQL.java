package br.com.ibnetwork.guara.metadata.impl;

import br.com.ibnetwork.guara.metadata.DatabaseMetadata;
import br.com.ibnetwork.guara.metadata.MetadataSupport;
import xingu.utils.JdbcUtils;

public class PostgreSQL
	extends MetadataSupport
	implements DatabaseMetadata
{	
	public void init()
		throws Exception
	{
		super.init();
		template = "/templates/orm/ddl/PostgreSQL.ftl";
		outputDirectory = mainResource.getDirectory()+"/../../sql/postgresql"; 
		outputFile = outputDirectory+"/create-tables.sql";
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
