package br.com.ibnetwork.guara.app.file;

import java.io.File;

import org.apache.commons.fileupload.FileItem;

public interface FileRepository
{
	String ROLE = FileRepository.class.getName();
	
	String UGLY = "/resources/images";
	
	File getRootDir();
	
	String getTargetDir(Object bean, String dirName);

	File store(Object bean, FileItem fi, String dirName, String fileName)
		throws Exception;
	
	File store(Object bean, FileItem fi, String dirName, String fileName, boolean overwrite, boolean deleteOnExit)
		throws Exception;
}
