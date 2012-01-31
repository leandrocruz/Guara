package br.com.ibnetwork.guara.mojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public abstract class MojoSupport
    extends AbstractMojo
{
    /**
     * @parameter expression="${debug}
     */
    private boolean debug = false;

    /**
     * @parameter expression="${overwrite}
     * 
     */
    protected boolean overwrite = true;

//    /**
//     * Project classpath.
//     *
//     * @parameter expression="${project.compileClasspathElements}"
//     * @required
//     * @readonly
//     */
//    @SuppressWarnings("unchecked")
//	private List classpathElements;

    /**
     * @parameter expression="${project}"
     */
    protected MavenProject project;
    
    /**
     * @parameter expression="${modulePackage}"  default-value="guara.modules.actions"
     */
    protected String modulePackage;

    /**
     * @parameter expression="${model}" 
     */
    protected String model;
    
    /**
     * @parameter
     */
    protected List<String> modelNames;
    
    /**
     * @parameter
     */
    protected Map<String, String> database = new HashMap<String, String>();
    
	@SuppressWarnings("unchecked")
	protected void printEnv()
	{
		if(debug)
		{
			Log log = getLog();
			log.info("-- Plugin Environment --");
			log.info("Group id: "+project.getGroupId());
			log.info("Source directory: "+project.getBuild().getSourceDirectory());
			log.info("Test Source directory: "+project.getBuild().getTestSourceDirectory());
			List<Resource> resources = project.getResources();
            for (Resource resource : resources) {
                log.info("Resource: " + resource.getDirectory());
            }
			
			List<Resource> testResources = project.getTestResources();
            for (Resource resource : testResources) {
                log.info("Test Resource: " + resource.getDirectory());
            }
			//log.info("Classpath: "+classpathElements.toString().replace( ',', '\n' ));
			log.info("");
		}
	}
}