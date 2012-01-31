package br.com.ibnetwork.guara;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.ibnetwork.guara.bean.BeanUtils;

public class BeanUtilsTest
{
    @Test
    public void testGetBeanName()
		throws Exception
	{
		assertEquals("sampleBean", BeanUtils.getBeanName(SampleBean.class));
		assertEquals("sampleBean", BeanUtils.getBeanName(new SampleBean()));
		assertEquals("sampleObject", BeanUtils.getBeanName(SampleObject.class));
		assertEquals("sampleObject", BeanUtils.getBeanName(new SampleObject()));
		assertEquals("beanNameAware", BeanUtils.getBeanName(BeanNameAware.class));
		assertEquals("XYZ", BeanUtils.getBeanName(new BeanNameAware()));
	}

    @Test
	public void testGetIndex()
	{
		assertEquals("objectName",BeanUtils.split("objectName[0].fieldName").objectName);
		assertEquals(0,BeanUtils.split("objectName[0].fieldName").index);
		assertEquals("fieldName",BeanUtils.split("objectName[0].fieldName").propertyName);
		assertEquals("objectName",BeanUtils.split("objectName[1].fieldName").objectName);
		assertEquals(1,BeanUtils.split("objectName[1].fieldName").index);
		assertEquals("fieldName",BeanUtils.split("objectName[1].fieldName").propertyName);
//		assertEquals("objectName",BeanUtils.split("objectName[].fieldName")[0]);
//		assertEquals(null,BeanUtils.split("objectName[].fieldName")[1]);
//		assertEquals("fieldName",BeanUtils.split("objectName[].fieldName")[2]);
		assertEquals("objectName",BeanUtils.split("objectName.fieldName").objectName);
		assertEquals(-1,BeanUtils.split("objectName.fieldName").index);
		assertEquals("fieldName",BeanUtils.split("objectName.fieldName").propertyName);
	}
}

class SampleObject
{
	
}
