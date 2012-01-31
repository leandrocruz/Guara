package br.com.ibnetwork.guara.mojo;


import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.handler.MacroReplacer;
import br.com.ibnetwork.guara.metadata.handler.TableReplacer;
import br.com.ibnetwork.guara.metadata.impl.AjaxScreen;
import br.com.ibnetwork.guara.metadata.impl.ControllerSupport;
import br.com.ibnetwork.guara.metadata.impl.EmptyController;
import br.com.ibnetwork.guara.metadata.impl.FreemarkerBeanMacro;
import br.com.ibnetwork.guara.metadata.impl.IndexScreen;
import br.com.ibnetwork.guara.metadata.impl.ListScreen;
import br.com.ibnetwork.guara.metadata.impl.PostgreSQL;
import br.com.ibnetwork.guara.metadata.impl.SqlMap;
import br.com.ibnetwork.guara.metadata.impl.SqlMapCrud;

/**
 * @goal generate-all
 * @execute phase="compile"
 * @phase package 
 * @requiresInjectResolution compile
 */
public class GenerateAllMojo
	extends TextGenerator
{
	protected Metadata[] getTasks()
	{
		Metadata[] array = new Metadata[]{
				create(ControllerSupport.class),
				create(EmptyController.class),
				create(PostgreSQL.class, new TableReplacer()),
				create(SqlMapCrud.class),
				create(SqlMap.class),
				//create(CrudTest.class),
				//create(Tool.class),
				//create(EditScreen.class),
				//create(ShowScreen.class),
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
		generate(meta);
    }

	
}
