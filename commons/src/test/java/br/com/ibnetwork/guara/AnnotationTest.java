package br.com.ibnetwork.guara;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

import br.com.ibnetwork.guara.annotation.ColumnInfo;
import br.com.ibnetwork.guara.annotation.InputInfo;
import br.com.ibnetwork.guara.myapp.om.SingleField;
import xingu.utils.annotation.AnnotationHelper;

public class AnnotationTest
{
    
    @Test
    public void testGetFieldAnnotationByType()
		throws Exception
	{
		SingleField test = new SingleField();
		Class clazz = test.getClass();
		ColumnInfo database = (ColumnInfo) AnnotationHelper.getFieldAnnotationByType(clazz, "field", ColumnInfo.class);
		assertEquals("SAMPLE_DATE",database.name());
		assertEquals(ColumnInfo.Type.TIMESTAMP,database.jdbcType());
	}
	
    @Test
	public void testGetAnnotationByType()
		throws Exception
	{
		SingleField test = new SingleField();
		Class clazz = test.getClass();
		Field field = clazz.getDeclaredField("field");
		
		ColumnInfo database = (ColumnInfo) AnnotationHelper.getAnnotationByType(field, ColumnInfo.class);
		assertEquals("SAMPLE_DATE",database.name());
		assertEquals(ColumnInfo.Type.TIMESTAMP,database.jdbcType());
		
		InputInfo input = (InputInfo) AnnotationHelper.getAnnotationByType(field, InputInfo.class);
		assertEquals("text",input.inputType());
		assertEquals("dd/MM/yyyy",input.format());
	}
}