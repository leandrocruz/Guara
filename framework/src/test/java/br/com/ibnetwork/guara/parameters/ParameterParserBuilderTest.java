package br.com.ibnetwork.guara.parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.guara.test.GuaraTestCase;
import br.com.ibnetwork.guara.test.mock.MockHttpServletRequest;
import br.com.ibnetwork.xingu.container.Inject;

public class ParameterParserBuilderTest
	extends GuaraTestCase
{
	@Inject
    private ParameterParserBuilder builder;

	@Test
	public void testEmptyRequest()
    	throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        HttpServletRequest mock = new MockHttpServletRequest(map,null);
        Parameters parser = builder.createParameterParser(mock);
        assertNull(parser.get("key"));
        assertNull(parser.getStrings("key"));
    }

	@Test
	public void testRequestValues()
		throws Exception
	{
        Map<String, String> map = new HashMap<String, String>();
        map.put("key","value");
        HttpServletRequest mock = new MockHttpServletRequest(map,null);
        Parameters parser = builder.createParameterParser(mock);
        assertEquals("value",parser.get("key"));
	}

	@Test
	@Ignore
	public void testPathInfo()
		throws Exception
	{
        Map<String, String> map = new HashMap<String, String>();
        HttpServletRequest mock = new MockHttpServletRequest(map,"/key/value");
        Parameters parser = builder.createParameterParser(mock);
        assertEquals("value",parser.get("key"));
	}

	@Test
	@Ignore
    public void testPathInfoAndRequestValuesSameVar()
		throws Exception
	{
        Map<String, String> map = new HashMap<String, String>();
        map.put("key","value2");
        HttpServletRequest mock = new MockHttpServletRequest(map,"/key/value1");
        Parameters parser = builder.createParameterParser(mock);
        String[] values = parser.getStrings("key");
        assertEquals("value1",values[0]);
        assertEquals("value2",values[1]);
	}

	@Test
    public void testGetAllPrimitiveKindsFromParser()
		throws Exception
	{
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("int","1");
	    map.put("byte","1");
	    map.put("boolean","true");
	    map.put("double","1.1d");
	    map.put("float","1.1f");
	    map.put("long","1");
	    HttpServletRequest mock = new MockHttpServletRequest(map,null);
	    Parameters parser = builder.createParameterParser(mock);
	    
	    int i = parser.getInt("int");
	    assertEquals(1, i);
	    
	    boolean bool = parser.getBoolean("boolean");
	    assertTrue(bool);
	    
	    double d = parser.getDouble("double");
	    assertTrue(d == 1.1d);
	    
	    float f = parser.getFloat("float");
	    assertTrue(f == 1.1f);
	    
	    long l = parser.getLong("long");
	    assertTrue(l == 1);
	}
    
	@Test
    public void testGetAllWrapKindsFromParser()
		throws Exception
	{
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("int","1");
	    map.put("byte","1");
	    map.put("boolean","true");
	    map.put("double","1.1d");
	    map.put("float","1.1f");
	    map.put("long","1");
	    map.put("date", "24/05/2005");
	    HttpServletRequest mock = new MockHttpServletRequest(map,null);
	    Parameters parser = builder.createParameterParser(mock);
	    
	    Integer i = parser.getIntObject("int");
	    assertEquals(1, i.intValue());
	    
	    Boolean bool = parser.getBooleanObject("boolean");
	    assertTrue(bool.booleanValue());
	    
	    Double d = parser.getDoubleObject("double");
	    assertEquals(1.1d, d.doubleValue(), 0);
	    
	    Float f = parser.getFloatObject("float");
	    assertEquals(1.1f, f.floatValue(), 0);
	    
	    Long l = parser.getLongObject("long");
	    assertEquals(1, l.longValue());
	    
	    Date date = parser.getDate("date");
	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	    Date date2 = df.parse("24/05/2005");
	    assertEquals(date2.getTime(), date.getTime());
	}
    
	@Test
    public void testSpaces()
        throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("boolean"," true ");
        map.put("int"," 1 ");
        map.put("long"," 1 ");
        map.put("float"," 1.1f ");
        map.put("double"," 1.1d ");

        HttpServletRequest mock = new MockHttpServletRequest(map,null);
        Parameters parser = builder.createParameterParser(mock);
        
        Integer i = parser.getIntObject("int");
        assertEquals(1, i.intValue());
        
        Integer[] iArray = parser.getIntObjects("int");
        assertEquals(1,iArray[0].intValue());

        Long l = parser.getLongObject("long");
        assertEquals(1, l.longValue());

        Long[] lArray = parser.getLongObjects("long");
        assertEquals(1, lArray[0].longValue());

        Float f = parser.getFloatObject("float");
        assertEquals(1.1f, f.floatValue(), 0);

        Float[] fArray = parser.getFloatObjects("float");
        assertEquals(1.1f, fArray[0].floatValue(), 0);

        Double d = parser.getDoubleObject("double");
        assertEquals(1.1d, d.doubleValue(), 0);

        Double[] dArray = parser.getDoubleObjects("double");
        assertEquals(1.1d, dArray[0].doubleValue(), 0);

        Boolean b = parser.getBooleanObject("boolean");
        assertEquals(true, b.booleanValue());

        Boolean[] bArray = parser.getBooleanObjects("boolean");
        assertEquals(true, bArray[0].booleanValue());

    }
    
	@Test
    public void testSetString()
    	throws Exception
    {
	    Map<String, String> map = new HashMap<String, String>();
        map.put("int","1");
	    HttpServletRequest mock = new MockHttpServletRequest(map,null);
	    Parameters parser = builder.createParameterParser(mock);
	    
	    Integer i = parser.getIntObject("int");
	    assertEquals(1, i.intValue());
	    
	    parser.setString("int", "200");
	    i = parser.getIntObject("int");
	    assertEquals(200, i.intValue());
    }
    
	@Test
    public void testClarParser()
    	throws Exception
    {
	    Map<String, String> map = new HashMap<String, String>();
        map.put("int","1");
	    HttpServletRequest mock = new MockHttpServletRequest(map,null);
	    Parameters parser = builder.createParameterParser(mock);
	    
	    Integer i = parser.getIntObject("int");
	    assertEquals(1, i.intValue());
	    parser.clear();
	    i = parser.getIntObject("int");
	    assertNull(i);
    }
	
	@Test
    public void testDateFormat()
    	throws Exception
    {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("date1", "02/03/2007");
	    map.put("date2", "02/MAR/2007");
	    map.put("date3", "02-MAR-2007");
	    
	    HttpServletRequest mock = new MockHttpServletRequest(map,null);
	    Parameters parser = builder.createParameterParser(mock);
	    
	    //date1
	    Date date = parser.getDate("date1","dd/MM/yyyy");
	    Calendar calendar =  Calendar.getInstance();
	    calendar.setTime(date);
	    assertEquals(2,calendar.get(Calendar.DAY_OF_MONTH));
	    assertEquals(Calendar.MARCH,calendar.get(Calendar.MONTH));
	    assertEquals(2007,calendar.get(Calendar.YEAR));

	    //date2
	    date = parser.getDate("date2","dd/MMM/yyyy");
	    calendar =  Calendar.getInstance();
	    calendar.setTime(date);
	    assertEquals(2,calendar.get(Calendar.DAY_OF_MONTH));
	    assertEquals(Calendar.MARCH, calendar.get(Calendar.MONTH));
	    assertEquals(2007,calendar.get(Calendar.YEAR));

	    //date3
	    date = parser.getDate("date3","dd-MMM-yyyy");
	    calendar =  Calendar.getInstance();
	    calendar.setTime(date);
	    assertEquals(2,calendar.get(Calendar.DAY_OF_MONTH));
	    assertEquals(Calendar.MARCH, calendar.get(Calendar.MONTH));
	    assertEquals(2007,calendar.get(Calendar.YEAR));
    }
    
	@Test
	public void testFileItem()
        throws Exception
	{
	    Map<String, String> map = new HashMap<String, String>();
	    HttpServletRequest mock = new MockHttpServletRequest(map,null);
	    Parameters params = builder.createParameterParser(mock);
        String fileFieldName = "fileField";
        String fileName = "originalFileName";
	    assertEquals(null,params.get(fileFieldName));

	    //add fi
	    FileItemFactory factory = new DefaultFileItemFactory(100000, null);;
        FileItem item = factory.createItem(
                fileFieldName,
                "application/octet-stream",
                false,
                fileName
        );

	    params.add(fileFieldName,item);
	    
	    //test
	    assertEquals(fileFieldName,item.getFieldName());
	    item = params.getFileItem(fileFieldName);
	    assertEquals(fileName,item.getName());
	    assertEquals(fileName,params.get(fileFieldName));
    }
}