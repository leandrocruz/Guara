package br.com.ibnetwork.guara.parameters.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import br.com.ibnetwork.guara.parameters.Parameters;
import br.com.ibnetwork.guara.parameters.ValueParserSupport;

public class ParametersImpl
	extends ValueParserSupport
	implements Parameters
{
    private Map<String, FileItem[]> fileItems = new HashMap<String, FileItem[]>();
    
    public ParametersImpl(String encoding)
    {
        super(encoding);
    }

    public FileItem getFileItem(String key)
    {
        FileItem[] value = fileItems.get(key);
        return value != null ? value[0] : null;
    }

    public FileItem[] getFileItems(String key)
    {
        return fileItems.get(key);
    }

    public void add(String key, FileItem item)
    {
        add(key, new FileItem[]{item});
    }

    public void add(String key, FileItem[] items)
    {
        for (FileItem item : items)
        {
	        add(key,item.getName());
        }
        FileItem[] values = fileItems.get(key);
        if(values != null)
        {
            int size = values.length + items.length;
            FileItem[] newValues = new FileItem[size];
            System.arraycopy(values,0,newValues,0,values.length);
            System.arraycopy(items,0,newValues,values.length,items.length);
            items = newValues;
        }
        fileItems.put(key,items);
    }
}
