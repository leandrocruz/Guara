package br.com.ibnetwork.guara.parameters;

import org.apache.commons.fileupload.FileItem;

public interface Parameters
	extends ValueParser
{
    int NONE_CASE_FOLDING = 0;
    
    int LOWER_CASE_FOLDING = 1;
    
    int UPPER_CASE_FOLDING = 2;

    FileItem getFileItem(String key);
    
    FileItem[] getFileItems(String key);

    void add(String key, FileItem item);

    void add(String key, FileItem[] item);
}
