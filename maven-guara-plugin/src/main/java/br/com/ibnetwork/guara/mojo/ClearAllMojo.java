package br.com.ibnetwork.guara.mojo;


import java.io.File;

import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.handler.MacroReplacer;
import br.com.ibnetwork.guara.metadata.impl.AjaxScreen;
import br.com.ibnetwork.guara.metadata.impl.ControllerSupport;
import br.com.ibnetwork.guara.metadata.impl.CrudTest;
import br.com.ibnetwork.guara.metadata.impl.EditScreen;
import br.com.ibnetwork.guara.metadata.impl.EmptyController;
import br.com.ibnetwork.guara.metadata.impl.FreemarkerBeanMacro;
import br.com.ibnetwork.guara.metadata.impl.HsqlDb;
import br.com.ibnetwork.guara.metadata.impl.IndexScreen;
import br.com.ibnetwork.guara.metadata.impl.ListScreen;
import br.com.ibnetwork.guara.metadata.impl.ShowScreen;
import br.com.ibnetwork.guara.metadata.impl.SqlMap;
import br.com.ibnetwork.guara.metadata.impl.SqlMapCrud;
import br.com.ibnetwork.guara.metadata.impl.Tool;

/**
 * @goal clean
 * @phase initialize
 * @requiresInjectResolution compile
 */
public class ClearAllMojo
	extends TextGenerator
{
	protected Metadata[] getTasks()
	{
		Metadata[] array = new Metadata[]{
				create(ControllerSupport.class),
				create(EmptyController.class),
				create(HsqlDb.class),
				create(SqlMapCrud.class),
				create(SqlMap.class),
				create(CrudTest.class),
				create(Tool.class),
				create(EditScreen.class),
				create(ShowScreen.class),
				create(IndexScreen.class),
				create(ListScreen.class),
				create(AjaxScreen.class),
				create(FreemarkerBeanMacro.class, new MacroReplacer())
			};
		return array;
	}

	@Override
    protected void executeMeta(Metadata meta) 
		throws Exception
    {
		if(meta.deleteFileOnClean())
		{
			String fileName = meta.getOutputFile();
			File file = new File(fileName);
			if(file.exists())
			{
				getLog().info("removing file: "+file);
				file.delete();
			}
		}
    }
}
