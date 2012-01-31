package br.com.ibnetwork.guara.test.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

/**
 * @author leandro
 */
public class MockServletOutputStream
	extends ServletOutputStream
{
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    
    public void write(int b) throws IOException
    {
        buffer.write(b);
    }

    public String getContents()
    {
        return buffer.toString();
    }
}
