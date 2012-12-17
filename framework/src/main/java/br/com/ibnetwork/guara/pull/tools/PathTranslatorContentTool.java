package br.com.ibnetwork.guara.pull.tools;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;

import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class PathTranslatorContentTool
	extends ApplicationToolSupport
	implements Initializable, Configurable
{
	private String file;
	
	private Map<String, String> map;
	
	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		file = conf.getChild("file").getAttribute("name");
	}

	@Override
	public void initialize()
		throws Exception
	{
		map = new HashMap<String, String>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = cl.getResourceAsStream(file);
		
		@SuppressWarnings("unchecked")
		List<String> lines = IOUtils.readLines(is);
		for (String line : lines)
		{
			String[] values = line.split("=");
			String key = StringUtils.trimToNull(values[0]);
			if(key != null)
			{
				String value = StringUtils.trimToNull(values[1]); 
				map.put(key, value);
			}
		}
		IOUtils.closeQuietly(is);
	}

	public String getURI(String resource)
	{
		return map.get(resource);
	}
}
