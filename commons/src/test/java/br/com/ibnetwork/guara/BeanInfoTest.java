package br.com.ibnetwork.guara;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.guara.metadata.BeanInfo;
import br.com.ibnetwork.guara.metadata.FieldInfo;
import br.com.ibnetwork.guara.myapp.om.Bean;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class BeanInfoTest
    extends XinguTestCase
{
	private Locale locale = Locale.US;
	
	
	@Test
	public void testFieldValue()
		throws Exception
	{
	    SampleBean bean = new SampleBean();
		BeanInfo beanInfo = new BeanInfo(bean);
		FieldInfo field;
		
		//String
		field = beanInfo.getInputField("name");
		assertEquals(null, field.getValue());
		assertEquals("", field.getValueFormatted(locale));
		
		bean.setName("sample");
		assertEquals("sample", field.getValue());
		assertEquals("sample", field.getValueFormatted(locale));
		
		//Date
		field = beanInfo.getInputField("sampleDate");
		assertEquals(null, field.getValue());
		assertEquals("", field.getValueFormatted(locale));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 30);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR, 2000);
		Date date = calendar.getTime();
		bean.setSampleDate(date);
		assertEquals(date, field.getValue());
		assertEquals("30/01/2000", field.getValueFormatted(locale));
		
		//Formatted Long
		field = beanInfo.getInputField("formattedLong");
		assertEquals(new Long(0), field.getValue());
		assertEquals("0.00", field.getValueFormatted(locale));
		bean.setFormattedLong(3000);
		assertEquals(new Long(3000), field.getValue());
		assertEquals("3,000.00", field.getValueFormatted(locale));

		//Formatted Double
		field = beanInfo.getInputField("formattedDouble");
		assertEquals(new Double(0), field.getValue());
		assertEquals("0.00", field.getValueFormatted(locale));
		bean.setFormattedDouble(.15);
		assertEquals(new Double(.15), field.getValue());
		assertEquals("0.15", field.getValueFormatted(locale));
		
		//boolean
		field = beanInfo.getInputField("enabled");
		assertEquals(false, field.getValue());
		assertEquals("false", field.getValueFormatted(locale));
		
		//Reference
//		field = beanInfo.getInputField("parentId");
//		assertEquals(new Long(0),field.getValue());
//		assertEquals(null,field.getValueFormatted(locale));

	}
	
	@Test
	@Ignore
	public void testChoiceFormat()
		throws Exception
	{
		double[] limits = {1,2,3,4,5,6,7};
		 String[] monthNames = {"Sun","Mon","Tue","Wed","Thur","Fri","Sat"};
		 ChoiceFormat form = new ChoiceFormat(limits, monthNames);
		 ParsePosition status = new ParsePosition(0);
		 for (double i = 0.0; i <= 8.0; ++i) {
		     status.setIndex(0);
		     System.out.println(i + " -> " + form.format(i) + " -> "
		                              + form.parse(form.format(i),status));
		 }
	}
	
	@Ignore
	@Test
	public void testChoiceFormat2()
		throws Exception
	{
		 double[] filelimits = {0,1,2};
		 String[] filepart = {"are no files","is one file","are {2} files"};
		 ChoiceFormat fileform = new ChoiceFormat(filelimits, filepart);
		 Format[] testFormats = {fileform, null, NumberFormat.getInstance()};
		 MessageFormat pattform = new MessageFormat("There {0} on {1}");
		 pattform.setFormats(testFormats);
		 Object[] testArgs = {null, "ADisk", null};
		 for (int i = 0; i < 4; ++i) {
		     testArgs[0] = new Integer(i);
		     testArgs[2] = testArgs[0];
		     System.out.println(pattform.format(testArgs));
		 }
	}

	@Test
	public void testHasPrimaryKey()
	{
		BeanInfo beanInfo; 
		
		beanInfo = new BeanInfo(new SampleBean());
		assertFalse(beanInfo.hasPrimaryKey());
		
		beanInfo = new BeanInfo(new Bean());
		assertFalse(beanInfo.hasPrimaryKey());

		beanInfo = new BeanInfo(new Bean(1));
		assertTrue(beanInfo.hasPrimaryKey());
	}
}
