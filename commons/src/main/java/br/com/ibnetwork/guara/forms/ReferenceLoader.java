package br.com.ibnetwork.guara.forms;

import java.util.List;

import br.com.ibnetwork.guara.forms.impl.EmptyReferenceLoader;

public interface ReferenceLoader
{
	ReferenceLoader DEFAULT = EmptyReferenceLoader.instance();

	List<Option> loadOptions()
		throws Exception;
	
	List<Option> loadOptions(String selected)
		throws Exception;

	Option loadOption(String value)
		throws Exception;
	
	Option getDefaultOption()
		throws Exception;
}
