package br.com.ibnetwork.guara.mojo;

import org.apache.maven.plugin.MojoExecutionException;

//import xingu.container.Container;
//import xingu.container.ContainerUtils;

/**
 * @goal printEnv
 * @requiresInjectResolution compile
 * @phase initialize
 */
public class PrintEnv
    extends OldMojoSupport
{

//    /**
//     * @parameter expression="${encoding}"  default-value="UTF-8"
//     */
//    private String encoding;
//    
//    private Container pulga;
    
    public void execute()
        throws MojoExecutionException
    {
    	printEnv();
    	getLog().info("initializing Pulga... ");
    	
    	try
        {
    		init();
        }
        catch (Exception e)
        {
        	getLog().error("Aborting execution", e);
        }
    }

	private void init()
    {
//		pulga = ContainerUtils.getContainer();
	}
}
