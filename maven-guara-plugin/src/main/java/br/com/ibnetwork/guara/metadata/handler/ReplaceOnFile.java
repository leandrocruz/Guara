package br.com.ibnetwork.guara.metadata.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import br.com.ibnetwork.guara.metadata.Metadata;

public abstract class ReplaceOnFile
    implements FileHandler
{

	protected int start = -1;
	
	protected int end = -1;

	protected String text;
	
	protected boolean replaced = false;
	
	protected Metadata meta;
	
	public void handleFile(File file, String text, Metadata meta)
	    throws Exception
	{
		//create new file if necessary
		if(!file.exists())
		{
			WriteStringToFile.instance().handleFile(file, text, meta);
			return;
		}
		this.text = text;
		this.meta = meta;
		BufferedReader old = new BufferedReader(new FileReader(file));
		String line;
		int i = 0;
		
		StringBuffer sb = new StringBuffer();
		while((line = old.readLine()) != null)
		{
			processLine(i, line, sb);
			i++;
		}
		if(!replaced)
		{
			//didn't found code block, write the new one
			sb.append(text).append("\n");
		}
		WriteStringToFile.instance().handleFile(file, sb.toString(), meta);
	}

	private void processLine(int i, String line, StringBuffer sb)
    {
		if(checkStart(line))
		{
			start = i;
		}
		if(checkEnd(line))
		{
			//write new code block
			end = i;
			replaced = true;
			sb.append(text).append('\n');
		}
		if(checkInsideCodeBlock(i))
		{
			//ignore old definition
		}
		else
		{
			sb.append(line).append('\n');
		}
    }

	private boolean checkInsideCodeBlock(int lineNumber)
    {
		if(start < 0)
		{
			return false;	
		}
		return end < 0 || lineNumber <= end; 
    }

	protected abstract boolean checkEnd(String line);

	protected abstract boolean checkStart(String line);
}