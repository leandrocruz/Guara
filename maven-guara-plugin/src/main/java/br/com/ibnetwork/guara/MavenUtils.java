package br.com.ibnetwork.guara;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.project.MavenProject;

public class MavenUtils
{
	@SuppressWarnings("unchecked")
	public static ClassLoader getProjectClassLoader(MavenProject project) 
		throws Exception
	{
		List elements = project.getCompileClasspathElements();
		URL[] urls = new URL[elements.size()];
		for ( int i = 0; i < elements.size(); i++ )
		{
			Object obj = elements.get(i);
			if(obj != null)
			{
				if(obj instanceof File)
				{
					File file = (File) obj;
					urls[i] = file.toURI().toURL();
				}
				else
				{
					File tmp = new File((String) obj);
					urls[i] = tmp.toURI().toURL();
				}
			}
		}
		ClassLoader cl = new URLClassLoader(urls,MavenUtils.class.getClassLoader());
		return cl;
	}
}