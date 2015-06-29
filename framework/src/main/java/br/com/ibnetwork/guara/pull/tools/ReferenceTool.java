package br.com.ibnetwork.guara.pull.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.com.ibnetwork.guara.forms.Option;
import br.com.ibnetwork.guara.forms.ReferenceCache;
import br.com.ibnetwork.guara.forms.ReferenceLoader;
import br.com.ibnetwork.guara.forms.impl.OptionImpl;
import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;
import xingu.container.Inject;
import xingu.factory.Factory;

/**
 * @author leandro
 */
public class ReferenceTool 
	extends ApplicationToolSupport
{
	@Inject
	private Factory factory;
	
	public List<Option> load(String loaderClassName) 
		throws Exception
	{
		ReferenceLoader loader = getLoader(loaderClassName);
		return loader.loadOptions();
	}

	public Option loadOption(String loaderClassName, String value)
		throws Exception
	{
		ReferenceLoader loader = getLoader(loaderClassName);
		return loader.loadOption(value);
	}

	public ReferenceLoader getLoader(String loaderClass)
    {
		if(StringUtils.isEmpty(loaderClass))
		{
			return EmptyLoader.getInstance();
		}
		return ReferenceCache.getLoader(factory, loaderClass);
    }
}

class EmptyLoader
	implements ReferenceLoader
{
	private static ReferenceLoader INSTANCE = new EmptyLoader();
	
	private List<Option> options;
	
	private EmptyLoader()
	{
		options = new ArrayList<Option>(1);
		options.add(new OptionImpl("","--"));
	}

	public static ReferenceLoader getInstance()
	{
		return INSTANCE;
	}

	public Option loadOption(String value)
    {
	    return options.get(0);
    }

	public List<Option> loadOptions(String selected)
    {
	    return loadOptions();
    }

	public List<Option> loadOptions()
    {
	    return options;
    }

	public Option getDefaultOption()
    {
	    return options.get(0);
    }

}
