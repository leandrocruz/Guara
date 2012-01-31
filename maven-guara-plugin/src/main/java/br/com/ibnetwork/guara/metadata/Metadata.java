package br.com.ibnetwork.guara.metadata;

import org.apache.maven.project.MavenProject;

import br.com.ibnetwork.guara.metadata.handler.FileHandler;

public interface Metadata
{
	String WEB_APP = "WebContent";
	
	/**
	 * Every metadata object must be created using the MavenProject as parameter
	 */
	MavenProject getProject();
	
	void setProject(MavenProject project);
	
	void setModelName(String modelName);
	
	String getModelName();

	void setFileHandler(FileHandler handler);
	
	void init()
		throws Exception;
	
	/*
	 * Example: br.com.ibnetwork.om.User
	 */
	String getDomainClass();

	/*
	 * Example: User, derived from domainClass
	 */
	String getShortDomainClass();
	
	/*
	 * Example: user, derived from domainClass
	 */
	String getReferenceName();

	BeanInfo getBeanInfo()
		throws Exception;
	
	String getOutputFile();
	
	String getOutputDirectory();
	
	String getTemplate();

	void saveResults(String text, boolean overwrite)
		throws Exception;

	boolean deleteFileOnClean();
}
