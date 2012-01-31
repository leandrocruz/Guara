package ${meta.packageName};

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

import junit.framework.TestSuite;

import br.com.ibnetwork.guara.test.GuaraTestCase;
import br.com.ibnetwork.xingu.dao.Dao;
import br.com.ibnetwork.xingu.dao.DaoManager;
import br.com.ibnetwork.xingu.factory.Factory;

import ${meta.domainClass};

<#list meta.beanInfo.imports as import>
import ${import.type};
</#list>

public class ${meta.compilationUnit}
    extends GuaraTestCase
{
	private DaoManager daoManager;
	
	private Factory factory;
	
	private Dao<Long,${meta.shortDomainClass}> dao;
	
	private long ID;
	
	public ${meta.compilationUnit}()
	{
		super();
	}

	public ${meta.compilationUnit}(String testName)
	{
		super(testName);
	}
	
	public static TestSuite suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new ${meta.compilationUnit}("testGetById"));
		suite.addTest(new ${meta.compilationUnit}("testGetAll"));
		suite.addTest(new ${meta.compilationUnit}("testCreate"));
		suite.addTest(new ${meta.compilationUnit}("testUpdate"));
		suite.addTest(new ${meta.compilationUnit}("testDelete"));
		return suite;
	}

	
	@Override
    protected void setUp() 
		throws Exception
    {
	    super.setUp();
	    factory = (Factory) getContainer().lookup(Factory.class);
	    daoManager = (DaoManager) getContainer().lookup(DaoManager.class);
	    dao = daoManager.getDao(${meta.shortDomainClass}.class);
    }

	@Override
    protected void tearDown() 
		throws Exception
    {
	    super.tearDown();
	    factory = null;
	    daoManager = null;
	    dao = null;
    }

	public void testGetById()
		throws Exception
	{
		${meta.shortDomainClass} ${meta.referenceName} = (${meta.shortDomainClass}) dao.getById(new Long(1));
		assertEquals(1,${meta.referenceName}.getId());
	}
	
	public void testGetAll()
		throws Exception
	{
		List<${meta.shortDomainClass}> all = dao.getAll();
		assertEquals(1,all.size());
	}
	
	public void testCreate()
		throws Exception
	{
		${meta.shortDomainClass} ${meta.referenceName} = (${meta.shortDomainClass}) factory.create(${meta.shortDomainClass}.class);
		assertEquals(0,${meta.referenceName}.getId());
		
		List<${meta.shortDomainClass}> all = dao.getAll();
		int size = all.size();
		
		dao.insert(${meta.referenceName});
		ID = ${meta.referenceName}.getId();
		
		assertTrue(ID > 0);
		all = dao.getAll();
		assertEquals(size + 1, all.size());
	}
	
	public void testUpdate()
		throws Exception
	{
		<#list meta.beanInfo.columns as property>
			<#if property.name != "id">
				<#switch property.type>
				  <#case "byte">
				  <#case "short">
				  <#case "int">
				  <#case "long">
				  <#case "java.lang.Byte">
				  <#case "java.lang.Short">
				  <#case "java.lang.Integer">
				  <#case "java.lang.Long">
		${property.typeName} ${property.columnName} = 1;
				     <#break>
				  <#case "float">
				  <#case "double">
				  <#case "java.lang.Float">
				  <#case "java.lang.Double">
		${property.typeName} ${property.columnName} = 1.2;
				     <#break>
				  <#case "boolean">
				  <#case "java.lang.Boolean">
		${property.typeName} ${property.columnName} = true;
				     <#break>
				  <#case "java.lang.String">
		${property.typeName} ${property.columnName} = "Uma String";
				     <#break>
				  <#case "java.util.Date">
		${property.typeName} ${property.columnName} = new Date();
				     <#break>
				  <#case "java.math.BigDecimal">
		${property.typeName} ${property.columnName} = 2;
				     <#break>			     			     			     
				  <#default>
				  	<#if property.typeName != "List">
		${property.typeName} ${property.columnName} = new ${property.typeName}();
					</#if>
				</#switch>
			</#if>
		</#list>	
				
		${meta.shortDomainClass} ${meta.referenceName} = (${meta.shortDomainClass}) dao.getById(ID);
		assertEquals(ID, ${meta.referenceName}.getId());
		<#list meta.beanInfo.columns as property>
			<#if property.name != "id" && property.typeName != "List">
		assertEquals(${property.columnName}, ${meta.referenceName}.${property.getter}());
			</#if>
		</#list>
		List<${meta.shortDomainClass}> all = dao.getAll();
		int size = all.size();
		
		//update
		<#list meta.beanInfo.columns as property>
			<#if property.name != "id" && property.typeName != "List">
		${meta.referenceName}.${property.setter}(${property.columnName});
			</#if>
		</#list>		
		dao.update(${meta.referenceName});
		
		//asserts
		all = dao.getAll();
		assertEquals(size, all.size());
		${meta.referenceName} = (${meta.shortDomainClass}) dao.getById(ID);
		<#list meta.beanInfo.columns as property >
			<#if property.typeName != "List">
		assertEquals(${property.columnName}, ${meta.referenceName}.${property.getter}());
			</#if>
		</#list>			
		
	}
	
	public void testDelete()
		throws Exception
	{	
		${meta.shortDomainClass} ${meta.referenceName} = (${meta.shortDomainClass}) dao.getById(ID);
		assertEquals(ID, ${meta.referenceName}.getId());
		List<${meta.shortDomainClass}> all = dao.getAll();
		int size = all.size();	

		//delete
		dao.delete(ID);
		
		//asserts
		all = dao.getAll();
		assertEquals(size - 1, all.size());
		${meta.referenceName} = (${meta.shortDomainClass}) dao.getById(ID);
		assertNull(${meta.referenceName});
	}
}
