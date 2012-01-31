package br.com.ibnetwork.guara.modules.screens;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.commons.logging.Log;

import br.com.ibnetwork.guara.message.SystemMessage;
import br.com.ibnetwork.guara.message.SystemMessageBroker;
import br.com.ibnetwork.guara.modules.Module;
import br.com.ibnetwork.guara.rundata.Outcome;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.xingu.template.Context;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public abstract class DownloadSupport
	implements Module
{
	protected ServiceManager manager;

    protected SystemMessageBroker sysMsg;
    
    protected String name;
	
	protected Log logger;

    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Outcome doPerform(RunData data, Context context)
    	throws Exception
    {
		File file = getFile(data);
		
		if(file == null || !file.exists())
		{
			SystemMessage msg;
			Object[] params = null;
			if(file != null)
			{
			    params = new Object[]{file.getName()};
				msg = sysMsg.getSystemMessage("downloadUnavailable");
			}
			else
			{
				msg = sysMsg.getSystemMessage("downloadUnavailable",null);
			}
			data.setMessage(msg,params);
			return Outcome.UNKNOWN;
		}

		HttpServletResponse response = data.getResponse();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		response.setHeader("Content-Length",Long.toString(file.length()));
		FileInputStream fis = null;
		try
		{
		    fis = new FileInputStream(file.getPath());	
	    	OutputStream out = response.getOutputStream();
	    	byte[] buf = new byte[4 * 1024];  // 4K buffer
		    int bytesRead;
		    int bytesTotal = 0;
		    while ((bytesRead = fis.read(buf)) != -1) 
		    {
				bytesTotal +=bytesRead;
				out.write(buf, 0, bytesRead);
	    	}
			logger.info("file ["+file.getPath()+"] downloaded");
			doClean(file);
		}
		catch(Exception e)
		{
			logger.error("file ["+file.getPath()+"] download error",e);
		}
		finally 
		{
	    	try
	    	{
				if (fis != null) fis.close();
	    	}
	    	catch(Exception ignored)
	    	{}
		}
    	
        return Outcome.UNKNOWN;
    }

    protected abstract void doClean(File file)
    	throws Exception;

    protected abstract File getFile(RunData data)
    	throws Exception;

}
