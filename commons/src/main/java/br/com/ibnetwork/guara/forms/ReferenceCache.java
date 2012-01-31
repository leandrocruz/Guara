package br.com.ibnetwork.guara.forms;

import java.util.HashMap;
import java.util.Map;

import br.com.ibnetwork.xingu.factory.Factory;

public class ReferenceCache
{
	private static Map<String, ReferenceLoader> cache = new HashMap<String, ReferenceLoader>();
	
	public static ReferenceLoader getLoader(Factory factory, String loaderClass)
	{
	    ReferenceLoader loader = cache.get(loaderClass);
		if(loader == null)
		{
			Object obj = factory.create(loaderClass);
			if(obj instanceof ReferenceLoader)
			{
				loader = (ReferenceLoader) obj; 
			}
			else
			{
				loader = ReferenceLoader.DEFAULT;
			}
			cache.put(loaderClass, loader);
		}
	    return loader;
	}
}
