package br.com.ibnetwork.guara.injector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;
import org.junit.Test;

import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.test.GuaraTestCase;
import xingu.container.Inject;

public class InjectorTest
    extends GuaraTestCase
{
	@Inject
	private Injector injector;
	
	@Test
	public void testIndexedProperty()
		throws Exception
	{
		MyModule module = new MyModule();
		
		List list = LazyList.decorate(new ArrayList(), new Factory(){
			public Object create()
            {
	            return null;
            }
		});
		module.setPojos(list);
		
		Pojo p1 = new Pojo();
		p1.setId(1);
		Pojo p2 = new Pojo();
		p2.setId(2);
		
		Pojo prop = (Pojo) PropertyUtils.getIndexedProperty(module, "pojos[1]");
		PropertyUtils.setIndexedProperty(module, "pojos[1]", p1);
		
		
		prop = (Pojo) PropertyUtils.getIndexedProperty(module, "pojos[1]");
		assertEquals(1,prop.getId());
		assertSame(p1, prop);
		
		PropertyUtils.setIndexedProperty(module, "pojos[1]", p2);
		prop = (Pojo) PropertyUtils.getIndexedProperty(module, "pojos[1]");
		assertEquals(2,prop.getId());
		assertSame(p2,prop);
	}
	
	@Test
	public void testInjectList()
		throws Exception
	{
		Map<String, String> map = new HashMap<String, String>();

		map.put("pojos[0].id", "1");
		map.put("pojos[0].name", "name1");
		map.put("pojos[1].name", "name2");
		map.put("pojos[1].sampleDate::dd/MM/yyyy", "01/02/2002");
		
		MyModule module = new MyModule();
		RunData data = runData(map, null);
		injector.inject(module, data.getParameters());
		List<Pojo> pojos = module.getPojos();
		assertEquals(1, pojos.get(0).getId());
		assertEquals("name1", pojos.get(0).getName());
		assertEquals("name2", pojos.get(1).getName());
	}
	
	@Test
	public void testInjectPojo()
		throws Exception
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "10");
		map.put("sampleDate::dd/MM/yyyy", "01/02/2001");

		map.put("pojo1.id", "1");
		map.put("pojo1.name", "name1");
		map.put("pojo2.name", "name2");
		map.put("pojo1.sampleDate::dd/MM/yyyy", "01/02/2002");
		
		MyModule module = new MyModule();
		RunData data = runData(map, null);
		injector.inject(module, data.getParameters());
		
		assertEquals(10,module.getId());
		assertEquals(1,module.getPojo1().getId());
		
		assertEquals(null,module.getName());
		assertEquals("name1",module.getPojo1().getName());
		assertEquals("name2",module.getPojo2().getName());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(module.getSampleDate());
		assertEquals(1,calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.FEBRUARY,calendar.get(Calendar.MONTH));
		assertEquals(2001,calendar.get(Calendar.YEAR));

		calendar.setTime(module.getPojo1().getSampleDate());
		assertEquals(1,calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.FEBRUARY,calendar.get(Calendar.MONTH));
		assertEquals(2002,calendar.get(Calendar.YEAR));
	}

	@Test
	public void testInjectValues()
		throws Exception
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("s", "sample value");
		map.put("id", "1");
		map.put("sampleDate::dd/MM/yyyy", "01/02/2001");
		map.put("otherDate::dd-MMM-yyyy", "01-Fev-2001");
		map.put("nullValue", null);
		
		MyModule module = new MyModule();
		RunData data = runData(map, null);
		injector.inject(module, data.getParameters());
		
		assertEquals(1,module.getId());
		assertEquals("sample value",module.getS());
		assertEquals(null,module.getNullValue());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(module.getSampleDate());
		assertEquals(1,calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.FEBRUARY,calendar.get(Calendar.MONTH));
		assertEquals(2001,calendar.get(Calendar.YEAR));
		assertEquals("sample value",module.getS());

		calendar = Calendar.getInstance();
		calendar.setTime(module.getOtherDate());
		assertEquals(1,calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.FEBRUARY,calendar.get(Calendar.MONTH));
		assertEquals(2001,calendar.get(Calendar.YEAR));
	}

	@Test
	public void _testDescribe() 
		throws Exception
	{
		MyModule module = new MyModule();
		module.setSampleDate(new Date());
		module.setId(1);
		Map map = BeanUtils.describe(module);
		System.out.println(map);
	}
	
}