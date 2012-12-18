package br.com.ibnetwork.guara.parameters.impl;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.parameters.ParameterParserBuilder;
import br.com.ibnetwork.guara.parameters.Parameters;
import br.com.ibnetwork.guara.rundata.URLHandler;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;

public class ParameterParserBuilderImpl
    implements ParameterParserBuilder, Configurable
{
    @Inject
    protected Factory factory;

    @Inject
    protected URLHandler urlHandler;

    protected Log logger = LogFactory.getLog(getClass());

    protected ServletFileUpload uploadHandler;

    protected boolean fileUploadEnabled;

    protected int maxFileSize;

    protected int sizeThreshold;

    protected String encoding;

    public void configure(Configuration conf)
        throws ConfigurationException
    {
        encoding = conf.getChild("encoding").getAttribute("name", "UTF-8");

        // configure file upload
        Configuration fileUploadConfig = conf.getChild("fileUpload");
        fileUploadEnabled = fileUploadConfig.getAttributeAsBoolean("enabled", false);
        if (fileUploadEnabled)
        {
            enableFileUpload(fileUploadConfig);
        }
    }

    private void enableFileUpload(Configuration conf)
        throws ConfigurationException
    {
        maxFileSize = conf.getAttributeAsInteger("maxFileSize", 300000);
        sizeThreshold = conf.getAttributeAsInteger("sizeThreshold", 100000);
        String repository = conf.getAttribute("repository", null);
        File repo = null;
        boolean createRepository = conf.getAttributeAsBoolean("createRepository", true);
        if (repository == null && createRepository)
        {
            try
            {
                File temp = File.createTempFile("guara-", ".tmp");
                repo = temp.getParentFile();
                temp.deleteOnExit();
            }
            catch (IOException e)
            {
                throw new ConfigurationException("Can't create temp file", e);
            }
        }
        else
        {
            repo = new File(repository);
        }
        if (repo != null && !repo.exists() && createRepository)
        {
            logger.info("Creating repository [" + repo + "]");
            repo.mkdirs();
        }
        uploadHandler = new ServletFileUpload(new DiskFileItemFactory(sizeThreshold, repo));
        uploadHandler.setSizeMax(maxFileSize);
        uploadHandler.setHeaderEncoding(encoding);
    }

    public Parameters createParameterParser(HttpServletRequest request)
        throws Exception
    {
        Parameters params = factory.create(Parameters.class, encoding);
        if (fileUploadEnabled)
        {
            handleFileUpload(params, request);
        }
        
        extractPathInfo(params, request);
        
        // unused for file multipart/form-data request
        extractParameters(params, request);

        if (logger.isDebugEnabled())
        {
            logger.debug("Parameters found in the Request:");
            for (Iterator<String> it = params.keySet().iterator(); it.hasNext();)
            {
                String key = it.next();
                logger.debug("Key: " + key + " -> " + params.get(key));
            }
        }

        return params;
    }

    protected void handleFileUpload(Parameters parser, HttpServletRequest request)
    {
        if (!ServletFileUpload.isMultipartContent(request))
        {
            return;
        }
        logger.debug("Parsing file upload");
        try
        {
            // TODO: parse file upload. see TurbineUpload.parseRequest(request,
            // this);
            List fileItems = uploadHandler.parseRequest(request);
            for (Iterator iter = fileItems.iterator(); iter.hasNext();)
            {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField())
                {
                    String parameterName = item.getFieldName();
                    String value = item.getString(encoding);
                    parser.add(parameterName, value);
                }
                else
                {
                    String fieldName = item.getFieldName();
                    // TODO: add to rundata
                    parser.add(fieldName, item);
                    if (logger.isDebugEnabled())
                    {
                        String fileName = item.getName();
                        String contentType = item.getContentType();
                        boolean isInMemory = item.isInMemory();
                        long sizeInBytes = item.getSize();
                        logger.debug("Received file [" + fileName
                                + "] contentType[" + contentType
                                + "] isInMemory [" + isInMemory
                                + "] siInBytes [" + sizeInBytes + "]");

                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("File upload failed", e);
        }
    }

    protected void extractPathInfo(Parameters params, HttpServletRequest request)
        throws Exception
    {
        Map<String, String> map = urlHandler.handleURL(request);
        if(map != null)
        {
            for(String key : map.keySet())
            {
                params.add(key, map.get(key));
            }
        }
    }

    protected void extractParameters(Parameters params, HttpServletRequest request)
    {
        for (Enumeration names = request.getParameterNames(); names.hasMoreElements();)
        {
            String parameterName = (String) names.nextElement();
            String[] values = request.getParameterValues(parameterName);
            if (values != null && !StringUtils.isEmpty(values[0]) && values.length > 0)
            {
                params.add(parameterName, values);
            }
        }
    }
}
