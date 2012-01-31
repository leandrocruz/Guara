package br.com.ibnetwork.guara.metadata.handler;

import br.com.ibnetwork.guara.metadata.DatabaseMetadata;

public class TableReplacer
    extends ReplaceOnFile
{
	protected boolean checkStart(String line)
    {
		DatabaseMetadata dbMeta = (DatabaseMetadata) meta;
		String start = "DROP SEQUENCE "+dbMeta.getTableName().toLowerCase()+"_id_seq;";
		//System.out.println(start);
	    return !replaced && line.indexOf(start) >= 0;
    }

	protected boolean checkEnd(String line)
    {
		String end = ");";
	    return !replaced && start >=0 && line.indexOf(end) >= 0;
    }
}
