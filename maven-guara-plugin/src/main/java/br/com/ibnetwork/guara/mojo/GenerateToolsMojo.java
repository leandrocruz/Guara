package br.com.ibnetwork.guara.mojo;


import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.impl.Tool;

/**
 * @goal generate-tools
 * @execute phase="compile"
 * @phase package 
 * @requiresInjectResolution compile
 */
public class GenerateToolsMojo
	extends TextGenerator
{
	protected Metadata[] getTasks()
	{
		Metadata[] array = new Metadata[]{
				create(Tool.class)
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
