package br.com.ibnetwork.guara.pull.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.pull.ApplicationTool;
import br.com.ibnetwork.guara.rundata.RunData;

public class ApplicationToolSupport 
	implements ApplicationTool
{
    protected Log logger = LogFactory.getLog(this.getClass());
    
    protected RunData data;

    public void refresh(RunData data)
    {
        this.data = data;
    }
}
