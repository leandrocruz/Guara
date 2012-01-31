package br.com.ibnetwork.guara.forms.impl;

import java.util.Arrays;
import java.util.List;

import br.com.ibnetwork.guara.forms.Option;
import br.com.ibnetwork.guara.forms.ReferenceLoader;

public abstract class ReferenceLoaderSupport
    implements ReferenceLoader
{

	public Option loadOption(String wanted)
		throws Exception
	{
		Option[] options = getOptions();
		for (int i = 0; i < options.length; i++)
        {
	        Option op = options[i];
	        String value = op.getValue(); 
	        if(value != null && value.equals(wanted))
	        {
	        	return op;
	        }
        }
		return Option.DEFAULT;
	}


	public List<Option> loadOptions()
		throws Exception
	{
		Option[] options = getOptions();
		return Arrays.asList(options);
	}
	
	public List<Option> loadOptions(String selected)
		throws Exception
    {
	    return loadOptions();
    }


	protected abstract Option[] getOptions()
		throws Exception;

	public Option getDefaultOption()
		throws Exception
	{
		List<Option> options = loadOptions();
		for (Option option : options)
        {
	        if(option.isSelected())
	        {
	        	return option;
	        }
        }
		return null;
	}
}