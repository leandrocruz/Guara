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
	 * @parameter expression="${pulga}
	 */
	public static String pulga = "pulga-static.xml";

	/**
	 * @parameter expression="${path}
	 */
	public static String path = "";

	/**
	 * @parameter expression="${index}
	 */
	public static String index = "/index.html";

	/**
	 * @parameter expression="${resources}
	 */
	public static String resources = "/resources";

	/**
	 * @parameter expression="${jsResources}
	 */
	public static String jsResources = "/resources/js";

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
		log.info("Building from: " + src);
		log.info("To: " + dst);

		/* Hash Resources */
		Map<String, String> nameToHashedName = hashResourceFiles(src, dst);
		writeNameToHashedName(nameToHashedName);
		
		/* Write gl.js */
		String gl = remapJavascriptLocations(nameToHashedName);
		writeGl(dst, gl);

		/* Write index.html */
		String index = renderIndex();
		writeIndex(dst, index);

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

	private void writeNameToHashedName(Map<String, String> nameToHashedName)
		throws Exception
	{
		StringBuffer sb = new StringBuffer();
		Set<String> keys = nameToHashedName.keySet();
		for (String key : keys)
		{
			String value = nameToHashedName.get(key);
			sb.append(key).append("=").append(value).append("\n");
		}

		String target = project.getBuild().getOutputDirectory();
		File file = new File(target, "nameToHashedName.txt");
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

	private void writeGl(File build, String map)
		throws Exception
	{
		String toReplace = "return {};";
		File file = new File(build, path + jsResources + "/gl.js");
		String txt = IOUtils.toString(new FileInputStream(file));
		int idx = txt.indexOf(toReplace);
		txt = txt.substring(0, idx) + map + txt.substring(idx + toReplace.length());
		IOUtils.write(txt, new FileOutputStream(file));

	}

	private void writeIndex(final File build, String contents)
		throws Exception
	{
		File file = new File(build, path + index);
		IOUtils.write(contents, new FileOutputStream(file));
		log.info("Result written to " + file);
	}

	private String remapJavascriptLocations(Map<String, String> nameToHashedName)
	{
		int len = (jsResources).length() + 1;
		StringBuffer sb = new StringBuffer();
		sb.append("var nameToHashedName = {};\n");
		Set<String> keys = nameToHashedName.keySet();
		for (String key : keys)
		{
			if (key.endsWith(".js"))
			{
				String hashedName = nameToHashedName.get(key);
				String k = key.substring(len, key.length() - 3);
				hashedName = hashedName.substring(len + path.length(), hashedName.length() - 3);
				sb.append("\t\tnameToHashedName['").append(k).append("'] = '").append(hashedName).append("';\n");
			}
		}
		sb.append("\t\treturn nameToHashedName;");
		return sb.toString();
	}

	private Map<String, String> hashResourceFiles(File root, final File build)
		throws Exception
	{
		final int len = build.getAbsolutePath().length() + path.length();
		final Map<String, String> nameToHashedName = new HashMap<String, String>();
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
				nameToHashedName.put(key, value);

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
		
		return nameToHashedName;
	}

	private boolean skip(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}

		String name = file.getName();
		return "gl.js".equals(name) || "moment.js".equals(name);
	}

	private String renderIndex()
		throws Exception
	{
		ClassLoader cl = classLoaderFromProjectResources();
		String result = new RunGuara().execute(pulga, cl);
		return result;

	}
}
