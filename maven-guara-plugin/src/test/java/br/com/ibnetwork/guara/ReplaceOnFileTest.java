package br.com.ibnetwork.guara;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import br.com.ibnetwork.guara.metadata.Metadata;
import br.com.ibnetwork.guara.metadata.handler.FileHandler;
import br.com.ibnetwork.guara.metadata.handler.MacroReplacer;
import br.com.ibnetwork.guara.metadata.handler.TableReplacer;
import br.com.ibnetwork.guara.metadata.handler.WriteStringToFile;
import br.com.ibnetwork.guara.metadata.impl.TestMeta;

public class ReplaceOnFileTest
    extends GeneratorTestSupport
{
	String MACRO_TEXT = "<#macro otherBeanMacro a b c>\nDEF\n</#macro>";
	
	String TABLE_TEXT = "CREATE TABLE test (NAME VARCHAR);";
	
	@Test
	public void testReplaceMacro()
		throws Exception
	{
		String orig = "JUNK\n" 
				+ "<#macro my>\n" + "ABC\n" + "</#macro>\n\n" 
				+  MACRO_TEXT+"\n\n"
				+ "<#macro my>\n" + "GHI\n" + "</#macro>\n"
		        + "JUNK\n";

		String replaced = "JUNK\n" 
				+ "<#macro my>\n" + "ABC\n" + "</#macro>\n\n" 
				+ MACRO_TEXT.replace("DEF", "XYZ")+"\n\n" 
				+ "<#macro my>\n" + "GHI\n" + "</#macro>\n"
		        + "JUNK\n"; 
		
		callHandler(orig, replaced, MACRO_TEXT.replace("DEF", "XYZ"), new MacroReplacer());
	}
	
	@Test
	public void testAppendMacro()
	    throws Exception
	{
		String orig = "JUNK\n" 
				+ "<#macro my>\n" + "ABC\n" + "</#macro>\n\n" 
				+ "<#macro my>\n" + "GHI\n" + "</#macro>\n"
		        + "JUNK\n";

		String replaced = "JUNK\n" 
				+ "<#macro my>\n" + "ABC\n" + "</#macro>\n\n" 
				+ "<#macro my>\n" + "GHI\n" + "</#macro>\n"
		        + "JUNK\n"
				+ MACRO_TEXT.replace("DEF", "XYZ")+"\n"; 

		callHandler(orig, replaced, MACRO_TEXT.replace("DEF", "XYZ"), new MacroReplacer());
	}

	public void _testReplaceTable()
		throws Exception
	{
		String orig = "JUNK\n" 
			+ "/* START: test */\n\n" 
			+  TABLE_TEXT+"\n\n"
			+ "/* END: test */\n\n"
	        + "JUNK\n";

		String replaced = "JUNK\n" 
			+ "/* START: test */\n\n" 
			+  TABLE_TEXT+"\n\n"
			+ "/* END: test */\n\n"
	        + "JUNK\n";
	
		callHandler(orig, replaced, TABLE_TEXT.replace("NAME", "LASTNAME"), new TableReplacer());
	}
	
	private void callHandler(String orig, String replaced, String text, FileHandler handler)
        throws Exception
    {
	    
		Metadata meta = createMeta(TestMeta.class);
		File tmp = File.createTempFile("maven-guara-plugin.", ".txt");
		WriteStringToFile.instance().handleFile(tmp, orig, meta);
		handler.handleFile(tmp, text, meta);
		String result = FileUtils.readFileToString(tmp, "UTF-8");
		assertEquals(replaced, result);
		tmp.delete();
    }
}
