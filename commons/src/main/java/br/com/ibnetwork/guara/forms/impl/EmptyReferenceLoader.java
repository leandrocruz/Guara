package br.com.ibnetwork.guara.forms.impl;

import br.com.ibnetwork.guara.forms.Option;
import br.com.ibnetwork.guara.forms.ReferenceLoader;

public class EmptyReferenceLoader 
	extends ReferenceLoaderSupport 
{

	private static final ReferenceLoader INSTANCE = new EmptyReferenceLoader();
	
	public static ReferenceLoader instance()
	{
		return INSTANCE;
	}
	
	private EmptyReferenceLoader()
	{}
	
	private Option[] options = new Option[]{Option.DEFAULT};
	
	@Override
	protected Option[] getOptions() 
		throws Exception 
	{
		return options;
	}

}
