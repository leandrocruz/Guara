package br.com.ibnetwork.guara.mojo.site;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;

import br.com.ibnetwork.guara.mojo.MojoSupport;
import xingu.utils.MD5Utils;
import xingu.utils.StringUtils;
import xingu.utils.classloader.TryChildClassLoader;
import xingu.utils.io.CopyDirectory;
import xingu.utils.io.FileUtils;
import xingu.utils.io.FileVisitor;
import xingu.utils.io.TreeVisitor;

import com.jcabi.aether.Aether;

/**
 * @goal static
 * @requiresProject true
 * @requiresDirectInvocation false
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
	 * @parameter expression="${exclusions}
	 */
	public static List<String> exclusions;

	/**
	 * @parameter expression="${output}
	 */
	public static String output = "";
	
	/**
	 * @parameter expression="${comet}
	 */
	public CometConfig comet;
	
	@Override
	protected void go()
		throws Exception
	{
		File src = project.getBasedir();
		File out = createOutputDirectory();
		File target = new File(out, path);
		
		log.info("Application path is: " + path);
		log.info("Pulga is: " + pulga);
		log.info("Index is: " + index);
		log.info("Resources is: " + resources);
		log.info("Resources (js) is: " + jsResources);
		log.info("WebApp is: " + webapp);
		log.info("---------------------------------------------------------------");
		log.info("Building from: " + src);
		log.info("To: " + target);
		log.info("---------------------------------------------------------------");

		/* Hash Resources */
		
		Map<String, String> dict = buildDictionary(src, target);
		writeDictionary(dict);
		
		replaceStrings(target, dict);
		
		/* Write gl.js */
		writeGuaraJavascriptLoader(target, dict);

		/* Write index.html */
		writeIndex(target);
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

	private void replaceStrings(File target, Map<String, String> dict)
		throws Exception
	{
		String fortiusjs = dict.get("/resources/js/fortius.js");
		File file = new File(target, fortiusjs);
		String src = FileUtils.toString(file);
		
		File root = project.getParent().getBasedir();
		File gitDir = new File(root, ".git");
        
		Repository repo = RepositoryCache.open(RepositoryCache.FileKey.lenient(gitDir,FS.DETECTED), true);
		String branch = repo.getBranch();
		DateFormat df = new SimpleDateFormat("hh:mm dd/MM/yyyy");
		String version = project.getVersion() + " (branch:" +branch + " data:" + df.format(new Date()) + ")";
		src = StringUtils.replaceOn("@@VERSION@@", src, version);

		String address = comet.getHost() + ":" + comet.getPort();
		src = StringUtils.replaceOn("@@HOST@@", src, address);
		FileUtils.toFile(src, file);
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

		String dir = project.getBuild().getOutputDirectory();
		File file = new File(dir, dictionary);
		String contents = sb.toString();
		FileUtils.toFile(contents, file);
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
		
		List<Resource> resources = project.getResources();
		for (Resource resource : resources)
		{
			File file = new File(resource.getDirectory());
			cl.add(file);
		}
		
		Collection<Artifact> dependencies = loadProjectDependencies(project);
		for (Artifact dep : dependencies)
		{
			File file = dep.getFile();
			cl.add(file);
		}

		return cl;
	}

	private Collection<Artifact> loadProjectDependencies(MavenProject project)
		throws DependencyResolutionException
	{
		String id = project.getArtifact().toString();
		DefaultArtifact main = new DefaultArtifact(id);
		String home = System.getProperty("user.home");
		Aether aether = new Aether(project, home + "/.m2/repository");
		Collection<Artifact> dependencies = aether.resolve(main, "runtime");
		Iterator<Artifact> it = dependencies.iterator();
		while (it.hasNext())
		{
			Artifact artifact = it.next();
			String name = artifact.getGroupId() + ":" + artifact.getArtifactId();
			boolean skip = exclusions.contains(name);
			if(skip)
			{
				it.remove();
				log.info("Ignoring '" + artifact.getFile() + "'");
			}
			else
			{
				log.info("Adding '" + artifact.getFile() + "'");
			}
		}
		
		return dependencies;
	}

	private void writeIndex(File target)
		throws Exception
	{
		ClassLoader cl = classLoaderFromProjectResources();
		String txt = new RunGuara().execute(pulga, cl);

		File file = new File(target, index);
		IOUtils.write(txt, new FileOutputStream(file));
		log.info("Result written to " + file);
	}

	private void writeGuaraJavascriptLoader(File target, Map<String, String> dict)
		throws Exception
	{
		File file = new File(target, jsResources + "/gl.js");
		String template = FileUtils.toString(file);
		String txt = dictionaryToString(dict);
		String result = StringUtils.replaceOn("return {};", template, txt);
		FileUtils.toFile(result, file);
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
				hashedName = hashedName.substring(len, hashedName.length() - 3);
				sb.append("\t\tdictionary['").append(k).append("'] = '").append(hashedName).append("';\n");
			}
		}
		sb.append("\t\treturn dictionary;");
		return sb.toString();
	}

	private Map<String, String> buildDictionary(File root, final File target)
		throws Exception
	{
		final int len = target.getAbsolutePath().length();
		final Map<String, String> dict = new HashMap<String, String>();
		File src = new File(root, webapp + resources);
		File dst = new File(target, resources);
		
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
				String value = folder + hashedName;
				log.debug("Adding " + key + " = " + value);
				dict.put(key, value);

				org.apache.commons.io.FileUtils.moveFile(file, new File(file.getParentFile(), hashedName));
			}

			private String hashedName(File file, String hash)
			{
				String name = file.getName();
				int idx = name.lastIndexOf(".");
				String hashedName = name.substring(0, idx) + "_" + hash + ".cache." + name.substring(idx + 1);
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
