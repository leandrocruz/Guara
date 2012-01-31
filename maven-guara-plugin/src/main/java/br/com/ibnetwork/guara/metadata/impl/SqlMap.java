package br.com.ibnetwork.guara.metadata.impl;

import br.com.ibnetwork.guara.metadata.DatabaseMetadata;
import br.com.ibnetwork.guara.metadata.MetadataSupport;
import br.com.ibnetwork.xingu.utils.JdbcUtils;

public class SqlMap
	extends MetadataSupport
	implements DatabaseMetadata
{
	public void init()
    {
		template = "/templates/orm/ibatis/SqlMap.ftl";
		outputDirectory = mainResource.getDirectory()+"/iBatis"; 
		outputFile = outputDirectory+"/"+getReferenceName()+".xml";
    }

	public String getTableName()
    {
		return JdbcUtils.toColumnName(getReferenceName());
    }

	@Override
    protected boolean overrideResults()
    {
	    return false;
    }

}
