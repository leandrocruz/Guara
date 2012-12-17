package br.com.ibnetwork.guara.mojo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public class MojoSupport
	extends AbstractMojo
{
	/**
	 * @parameter expression="${debug}
	 */
	protected boolean debug = false;

	/**
	 * @parameter expression="${project.compileClasspathElements}"
	 * @required
	 * @readonly
	 */
	protected List<String> classpathElements;

	/**
	 * @parameter expression="${project}"
	 */
	protected MavenProject project;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	protected ArtifactRepository localRepository;

	protected Log log;

	protected void printEnv()
	{
		if (debug)
		{
			log.info("-- Plugin Environment --");
			log.info("file: " + project.getFile());
			log.info("base dir: " + project.getBasedir());
			log.info("project name: " + project.getName());
			log.info("project main artifact id: " + project.getArtifact().getArtifactId());
			log.info("project id: " + project.getId());
			log.info("group id: " + project.getGroupId());
			Build build = project.getBuild();
			log.info("source directory: " + build.getSourceDirectory());
			log.info("test source directory: " + build.getTestSourceDirectory());
			log.info("");
		}
	}

	@Override
	public void execute()
		throws MojoExecutionException, MojoFailureException
	{
		log = getLog();
		printEnv();
		try
		{
			go();
		}
		catch (Exception e)
		{
			throw new MojoExecutionException("Error executing mojo", e);
		}

	}

	protected void go()
		throws Exception
	{
		throw new UnsupportedOperationException();
	}

	protected void copyFile(File directory, File file)
		throws MojoExecutionException
	{
		try
		{
			FileUtils.copyFileToDirectory(file, directory);
		}
		catch (IOException e)
		{
			throw new MojoExecutionException("", e);
		}
	}
}
