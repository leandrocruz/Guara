package br.com.ibnetwork.guara.mojo;


import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.handler.MacroReplacer;
import br.com.ibnetwork.guara.metadata.impl.AjaxScreen;
import br.com.ibnetwork.guara.metadata.impl.FreemarkerBeanMacro;
import br.com.ibnetwork.guara.metadata.impl.IndexScreen;
import br.com.ibnetwork.guara.metadata.impl.ListScreen;

/**
 * @goal generate-templates
 * @execute phase="compile"
 * @phase package 
 * @requiresInjectResolution compile
 */
public class GenerateTemplatesMojo
	extends TextGenerator
{
	protected Metadata[] getTasks()
	{
		Metadata[] array = new Metadata[]{
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
