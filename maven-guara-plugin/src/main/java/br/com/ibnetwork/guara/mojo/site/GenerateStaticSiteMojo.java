package br.com.ibnetwork.guara.mojo.site;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Resource;

import br.com.ibnetwork.guara.mojo.MojoSupport;
import br.com.ibnetwork.xingu.utils.MD5Utils;
import br.com.ibnetwork.xingu.utils.StringUtils;
import br.com.ibnetwork.xingu.utils.classloader.TryChildClassLoader;
import br.com.ibnetwork.xingu.utils.io.CopyDirectory;
import br.com.ibnetwork.xingu.utils.io.FileUtils;
import br.com.ibnetwork.xingu.utils.io.FileVisitor;
import br.com.ibnetwork.xingu.utils.io.TreeVisitor;

/**
 * @goal static
 * @requiresProject true
 * @requiresDirectInvocation true
 * @requiresDependencyResolution compile
 */
public class GenerateStaticSiteMojo
	extends MojoSupport
{
	/**
	 * @parameter expression="${dictionary}
	 */
	private String dictionary = "dictionary.txt";

	/**
	 * @parameter expression="${pulga}
	 */
	public String pulga = "pulga-static.xml";

	/**
	 * @parameter expression="${path}
	 */
	public String path = "";

	/**
	 * @parameter expression="${index}
	 */
	public String index = "/index.html";

	/**
	 * @parameter expression="${resources}
	 */
	public String resources = "/resources";

	/**
	 * @parameter expression="${jsResources}
	 */
	public String jsResources = "/resources/js";

	/**
	 * @parameter expression="${webapp}
	 */
	public static String webapp = "src/main/webapp";

	/**
	 * @parameter expression="${output}
	 */
	public static String output = "";

	
	@Override
	protected void go()
		throws Exception
	{
		File src = project.getBasedir();
		File dst = createOutputDirectory();

		log.info("Application path is: " + path);
		log.info("Pulga is: " + pulga);
		log.info("Index is: " + index);
		log.info("Resources is: " + resources);
		log.info("Resources (js) is: " + jsResources);
		log.info("WebApp is: " + webapp);
		log.info("---------------------------------------------------------------");
		log.info("Building from: " + src);
		log.info("To: " + dst);
		log.info("---------------------------------------------------------------");

		/* Hash Resources */
		Map<String, String> dict = hashResourceFiles(src, dst);
		writeDictionary(dict);
		
		/* Write gl.js */
		writeGuaraJavascriptLoader(dst, dict);

		/* Write index.html */
		writeIndex(dst);
	}

	private File createOutputDirectory()
		throws Exception
	{
		File dst;
		if(!StringUtils.isEmpty(output))
		{
			dst = new File(output);
			if(dst.exists())
			{
				org.apache.commons.io.FileUtils.cleanDirectory(dst);
			}
			else
			{
				dst.mkdirs();
			}
		}
		else
		{
			dst = FileUtils.createTempDir("fortius-");
		}
		return dst;
	}

	private void writeDictionary(Map<String, String> dict)
		throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("path = ").append(path).append("\n");
		
		Set<String> keys = dict.keySet();
		for (String key : keys)
		{
			String value = dict.get(key);
			sb.append(key).append(" = ").append(value).append("\n");
		}

		String target = project.getBuild().getOutputDirectory();
		File file = new File(target, dictionary);
		IOUtils.write(sb, new FileOutputStream(file));
	}

	private ClassLoader classLoaderFromProjectResources()
		throws Exception
	{
		Thread currentThread = Thread.currentThread();
		ClassLoader parent = currentThread.getContextClassLoader();
		TryChildClassLoader cl = new TryChildClassLoader(parent, false);
		currentThread.setContextClassLoader(cl);
		
		String target = project.getBuild().getOutputDirectory();
		cl.add(new File(target + "/"));
		
		@SuppressWarnings("unchecked")
		List<Resource> resources = project.getResources();
		for (Resource resource : resources)
		{
			File file = new File(resource.getDirectory());
			cl.add(file);
		}
		
		@SuppressWarnings("unchecked")
		Set<Artifact> dependencies = project.getDependencyArtifacts();
		for (Artifact artifact : dependencies)
		{
			File file = artifact.getFile();
			if(file != null)
			{
				cl.add(file);
			}
			else
			{
				//log.warn("Missing file for artifact '" + artifact.getArtifactId() + "'");
			}
		}

		return cl;
	}

	private void writeIndex(File dst)
		throws Exception
	{
		ClassLoader cl = classLoaderFromProjectResources();
		String txt = new RunGuara().execute(pulga, cl);

		File file = new File(dst, path + index);
		IOUtils.write(txt, new FileOutputStream(file));
		log.info("Result written to " + file);
	}

	private void writeGuaraJavascriptLoader(File dst, Map<String, String> dict)
		throws Exception
	{
		String txt = dictionaryToString(dict);
		String toReplace = "return {};";
		File file = new File(dst, path + jsResources + "/gl.js");
		String template = IOUtils.toString(new FileInputStream(file));
		int idx = template.indexOf(toReplace);
		template = template.substring(0, idx) + txt + template.substring(idx + toReplace.length());
		IOUtils.write(template, new FileOutputStream(file));
	}

	private String dictionaryToString(Map<String, String> dict)
	{
		int len = (jsResources).length() + 1;
		StringBuffer sb = new StringBuffer();
		sb.append("var dictionary = {};\n");
		Set<String> keys = dict.keySet();
		for (String key : keys)
		{
			if (key.endsWith(".js"))
			{
				String hashedName = dict.get(key);
				String k = key.substring(len, key.length() - 3);
				hashedName = hashedName.substring(len + path.length(), hashedName.length() - 3);
				sb.append("\t\tdictionary['").append(k).append("'] = '").append(hashedName).append("';\n");
			}
		}
		sb.append("\t\treturn dictionary;");
		return sb.toString();
	}

	private Map<String, String> hashResourceFiles(File root, final File build)
		throws Exception
	{
		final int len = build.getAbsolutePath().length() + path.length();
		final Map<String, String> dict = new HashMap<String, String>();
		File src = new File(root, webapp + resources);
		File dst = new File(build, path + resources);
		
		new CopyDirectory().copyTree(src, dst);
		new TreeVisitor(dst, new FileVisitor()
		{

			@Override
			public void visit(File file)
				throws Exception
			{
				boolean skip = skip(file);
				if (skip)
				{
					return;
				}

				InputStream is = new FileInputStream(file);
				String contents = IOUtils.toString(is);
				String hash = MD5Utils.md5Hash(contents);
				String hashedName = hashedName(file, hash);
				String key = file.getAbsolutePath().substring(len);
				String folder = key.substring(0, key.indexOf(file.getName()));
				
				/*
				 * Add the 'path' so that we don't have to know it on development mode 
				 */
				String value = path + folder + hashedName;
				log.debug("Adding " + key + " = " + value);
				dict.put(key, value);

				org.apache.commons.io.FileUtils.moveFile(file, new File(file.getParentFile(), hashedName));
			}

			private String hashedName(File file, String hash)
			{
				String name = file.getName();
				int idx = name.lastIndexOf(".");
				String hashedName = name.substring(0, idx) + "_" + hash + name.substring(idx);
				return hashedName;
			}

		}).visit();
		
		return dict;
	}

	private boolean skip(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}

		String name = file.getName();
		if("gl.js".equals(name) || "moment.js".equals(name))
		{
			return true;
		}
		
		boolean accept = name.endsWith(".js") || name.endsWith(".css"); 
		return !accept;
	}
}
