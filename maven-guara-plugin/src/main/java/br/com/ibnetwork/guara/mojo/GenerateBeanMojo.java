package br.com.ibnetwork.guara.mojo;

import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.impl.Bean;

/**
 * @goal generate-bean
 * @execute phase="compile"
 * @phase package 
 * @requiresInjectResolution compile
 */
public class GenerateBeanMojo
	extends TextGenerator
{
	protected Metadata[] getTasks()
	{
		Metadata[] array = new Metadata[]{
				create(Bean.class)
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
