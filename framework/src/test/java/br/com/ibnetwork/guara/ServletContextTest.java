package br.com.ibnetwork.guara;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.ibnetwork.guara.test.GuaraTestCase;

public class ServletContextTest
    extends GuaraTestCase
{
    @Test
    public void testGetRealPath()
        throws Exception
    {
        String path = Guara.getRealPath("foo.txt");
        assertTrue(path.endsWith("foo.txt"));
//        File file = new File(path);
//        assertTrue(file.exists());
    }
}
