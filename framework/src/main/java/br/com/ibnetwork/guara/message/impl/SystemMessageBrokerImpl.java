package br.com.ibnetwork.guara.message.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.guara.message.SystemMessage;
import br.com.ibnetwork.guara.message.SystemMessageBroker;
import br.com.ibnetwork.guara.message.SystemMessageException;
import xingu.container.Environment;
import xingu.container.Inject;
import xingu.utils.FSUtils;


public class SystemMessageBrokerImpl
    implements SystemMessageBroker, Configurable,Initializable
{
    protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private Locale defaultLocale;

	private boolean isLenient;
	
	private Map<Locale, List<SystemMessage>> messages;
	
	private String messageRepoBasePath;
	
	private DefaultConfigurationBuilder reader;
	
	@Inject
	private Environment env;

	public void configure(Configuration conf) 
		throws ConfigurationException
	{
		String language = conf.getChild("localeConfig").getAttribute("language","en");
		String country = conf.getChild("localeConfig").getAttribute("country","US");
		defaultLocale = new Locale(language,country);
		messageRepoBasePath = conf.getChild("repository").getAttribute("path","${app.root}/resources/system-messages/");
		messageRepoBasePath = env.replaceVars(messageRepoBasePath);
		logger.info("SystemMessage repository is ["+messageRepoBasePath+"]");
		isLenient = conf.getChild("isLenient").getAttributeAsBoolean("value",false);
	}

	public void initialize() 
		throws Exception
	{
		File base = FSUtils.loadAsFile(messageRepoBasePath);
		if(base == null)
		{
			logger.warn("Can't find message directory: "+messageRepoBasePath);
			return;
		}
		reader = new DefaultConfigurationBuilder();
		File localeDirs[] = base.listFiles((FileFilter)DirectoryFileFilter.INSTANCE);
		if(localeDirs != null)
		{
		    messages = new HashMap<Locale, List<SystemMessage>>(localeDirs.length);
		    for (int i = 0; i < localeDirs.length; i++)
		    {
		        File localeDir = localeDirs[i];
		        Locale locale = buildLocaleFromFileName(localeDir);
		        List<SystemMessage> localeMessages = extractMessages(localeDir);
		        messages.put(locale,localeMessages);
		    }
		}
	}

    private List<SystemMessage> extractMessages(File baseDir)
    	throws Exception
    {
		List<SystemMessage> result = new ArrayList<SystemMessage>();
        String fileNames[] = baseDir.list(new SuffixFileFilter(".xml"));
        for (int j = 0; j < fileNames.length; j++)
        {
            String fileName = baseDir.getAbsolutePath() + File.separator + fileNames[j];
			SystemMessage[] fileMessages = readMessagesFromFile(fileName);
            if(fileMessages == null)
            {
            	return result;
            }
        	for (int k = 0; k < fileMessages.length; k++)
            {
                SystemMessage message = fileMessages[k];
                result.add(message);
        		logger.debug("Adding message id["+message.getId()+"] from file["+fileName+"]");
            }
        }    
        return result;
    }

    private Locale buildLocaleFromFileName(File dir)
    {
        String dirName = dir.getName();
        String[] parts = StringUtils.split(dirName,"_");

		String language = parts[0];
        if(parts.length > 1)
        {
			String country = parts[1];
			return new Locale(language,country);        	
        }
        else
        {
			return new Locale(language);
        }
    }

    private SystemMessage[] readMessagesFromFile(String fileName)
    	throws Exception
    {
      	logger.info("Reading messages from file ["+fileName+"]");
      	Configuration[] conf = reader.buildFromFile(fileName).getChildren("message");
		SystemMessage[] result = new SystemMessage[conf.length];
		for (int i = 0; i < conf.length; i++)
        {
            Configuration msgConf = conf[i];
            SystemMessage message = createMessageInstance(msgConf);
			result[i] = message;            
        }
        return result;
    }

    private SystemMessage createMessageInstance(Configuration msgConf)
    	throws Exception
    {
		String id = msgConf.getAttribute("id");
		String type = msgConf.getAttribute("type",SystemMessage.DEFAULT_MESSAGE_TYPE);
		String text = msgConf.getValue();
        SystemMessage msg = new SystemMessageImpl(id,text,type);
        return msg;
    }

    public Locale getDefaultLocale()
    {
        return defaultLocale;
    }

    public boolean isLenient()
    {
        return isLenient;
    }
    
    public SystemMessage getSystemMessage(String msgId) 
    	throws SystemMessageException
    {
        return getSystemMessage(msgId,defaultLocale);
    }

    public SystemMessage getSystemMessage(String msgId, Locale locale)
        throws SystemMessageException
    {
		return findMessage(msgId,locale);
    }

	private SystemMessage findMessage(String msgId,Locale locale)
		throws SystemMessageException
	{
	    List<SystemMessage> localeMessages = messages.get(locale);
		for (Iterator<SystemMessage> it = localeMessages.iterator(); it.hasNext();)
		{
			SystemMessage msg = it.next();
			if(msg.getId().equals(msgId))
			{
				return msg;
			}
		}
		if(isLenient)
		{
		    return null;
		}
		throw new SystemMessageException("Message id["+msgId+"] not found.Locale ["+locale+"]");
	}
}
