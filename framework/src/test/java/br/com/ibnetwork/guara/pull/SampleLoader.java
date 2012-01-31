package br.com.ibnetwork.guara.pull;

import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.guara.forms.Option;
import br.com.ibnetwork.guara.forms.impl.OptionImpl;
import br.com.ibnetwork.guara.forms.ReferenceLoader;

public class SampleLoader
    implements ReferenceLoader
{

	public List<Option> loadOptions()
	{
		List<Option> result = new ArrayList<Option>();
		result.add(new OptionImpl(1,"First"));
		return result;
	}

	public List<Option> loadOptions(String selected)
    {
	    return loadOptions();
    }

	public Option loadOption(String option)
    {
	    return null;
    }

	public Option getDefaultOption()
    {
	    return null;
    }


}
