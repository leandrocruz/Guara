package br.com.ibnetwork.guara.metadata;


public abstract class JavaMetadataSupport
    extends MetadataSupport 
    implements JavaMetadata
{
	protected String compilationUnit;
	
	protected String packageName;
	
	protected String baseClass;

	protected String baseDir;

	public void init()
		throws Exception
    {
		baseDir = project.getBuild().getSourceDirectory();
		compilationUnit = shortDomainClass;
    }
	
	@Override
    protected boolean overrideResults()
    {
	    return false;
    }

	public String getCompilationUnit() 
	{
		return compilationUnit;
	}
	
	public String getPackageName()
    {
    	return packageName;
    }

	public String getBaseClass()
    {
    	return baseClass;
    }
	

}
