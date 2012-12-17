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
	 * @parameter expression="${path}
	 */
	public static String path = "";
	
	@Override
	protected void go()
		throws Exception
	{
		File src = project.getBasedir();
		File dst = FileUtils.createTempDir("fortius-");

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
				log.warn("Missing file for artifact '" + artifact.getArtifactId() + "'");
			}
		}

		return cl;
	}

	private void writeGl(File build, String map)
		throws Exception
	{
		String toReplace = "return {};";
		File file = new File(build, path + "/resources/js/gl.js");
		String txt = IOUtils.toString(new FileInputStream(file));
		int idx = txt.indexOf(toReplace);
		txt = txt.substring(0, idx) + map + txt.substring(idx + toReplace.length());
		IOUtils.write(txt, new FileOutputStream(file));

	}

	private void writeIndex(final File build, String contents)
		throws Exception
	{
		File file = new File(build, path + "/index.html");
		IOUtils.write(contents, new FileOutputStream(file));
		log.info("Result written to " + file);
	}

	private String remapJavascriptLocations(Map<String, String> nameToHashedName)
	{
		int len = (path + "/resources/js/").length() + 1;
		StringBuffer sb = new StringBuffer();
		sb.append("var nameToHashedName = {};\n");
		Set<String> keys = nameToHashedName.keySet();
		for (String key : keys)
		{
			if (key.endsWith(".js"))
			{
				String path = nameToHashedName.get(key);
				key = key.substring(len, key.length() - 3);
				path = path.substring(len, path.length() - 3);
				sb.append("\t\tnameToHashedName['").append(key).append("'] = '").append(path).append("';\n");
			}
		}
		sb.append("\t\treturn nameToHashedName;");
		return sb.toString();
	}

	private Map<String, String> hashResourceFiles(File root, final File build)
		throws Exception
	{
		final Map<String, String> nameToHashedName = new HashMap<String, String>();
		File resources = new File(root, "src/main/webapp/resources");
		File dest = new File(build, path + "/resources");
		new CopyDirectory().copyTree(resources, dest);
		new TreeVisitor(dest, new FileVisitor()
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

				log.debug("Visiting file: " + file);
				InputStream is = new FileInputStream(file);
				String contents = IOUtils.toString(is);
				String hash = MD5Utils.md5Hash(contents);
				String name = hash + "_" + file.getName();
				String key = file.getAbsolutePath().substring(build.getAbsolutePath().length());
				String path = key.substring(0, key.indexOf(file.getName())) + name;
				nameToHashedName.put(key, path);
				org.apache.commons.io.FileUtils.moveFile(file, new File(file.getParentFile(), name));
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
		String pulga = "pulga-static.xml";
		ClassLoader cl = classLoaderFromProjectResources();
		String result = new RunGuara().execute(pulga, cl);
		return result;

	}
}
