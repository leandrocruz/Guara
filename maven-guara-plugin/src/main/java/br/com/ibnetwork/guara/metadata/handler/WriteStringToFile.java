package br.com.ibnetwork.guara.metadata.handler;

import java.io.File;

import org.apache.commons.io.FileUtils;

import br.com.ibnetwork.guara.metadata.Metadata;


public class WriteStringToFile
    implements FileHandler
{
	private static WriteStringToFile INSTANCE = new WriteStringToFile();
	
	private WriteStringToFile()
	{}
	
	public static FileHandler instance()
    {
	    return INSTANCE;
    }

	public void handleFile(File file, String text, Metadata meta)
	    throws Exception
	{
		//System.out.println("Writing string to file: "+file);
		FileUtils.writeStringToFile(file, text, "UTF-8");
	}


}
