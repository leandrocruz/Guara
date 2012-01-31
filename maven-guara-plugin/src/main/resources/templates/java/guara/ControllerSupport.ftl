package ${meta.packageName};
<#assign fileItems = meta.beanInfo.fileItems />

<#if (fileItems?size > 0) >
import br.com.ibnetwork.guara.parameters.Parameters;
import org.apache.commons.lang.StringUtils;
</#if>

import br.com.ibnetwork.guara.app.modules.actions.SingleCrudControlSupport;
import br.com.ibnetwork.guara.modules.ThreadSafe;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.xingu.store.PersistentBean;

import ${meta.domainClass};

@ThreadSafe
public abstract class ${meta.compilationUnit}
    extends SingleCrudControlSupport
{
	private ${meta.shortDomainClass} _${meta.referenceName};

	public ${meta.shortDomainClass} get${meta.shortDomainClass}()
    {
    	return _${meta.referenceName};
    }

	public void set${meta.shortDomainClass}(${meta.shortDomainClass} ${meta.referenceName})
    {
    	_${meta.referenceName} = ${meta.referenceName};
    }
    
	@Override
    protected ${meta.shortDomainClass} createFromRequest(RunData data) 
		throws Exception
    {
	    return get${meta.shortDomainClass}();
    }

	protected Class<${meta.shortDomainClass}> getBeanClass()
    {
	    return ${meta.shortDomainClass}.class;
    }

	<#if (fileItems?size > 0) >
	@Override
	protected void beforeUpdate(RunData data, PersistentBean bean) 
		throws Exception 
	{
		${meta.shortDomainClass} ${meta.referenceName} = (${meta.shortDomainClass}) bean;
		<#list fileItems as item>

		//Check if we need to "restore" ${meta.referenceName}.${item.name}
		if(StringUtils.isEmpty(${meta.referenceName}.${item.getter}()))
		{
			${meta.shortDomainClass} old = store.getById(${meta.shortDomainClass}.class, ${meta.referenceName}.getId());
			${meta.referenceName}.${item.setter}(old.${item.getter}());
		}
		</#list>
	}

	@Override
    protected void afterStore(RunData data, PersistentBean bean)
        throws Exception
    {
	    ${meta.shortDomainClass} ${meta.referenceName} = (${meta.shortDomainClass}) bean;
	    
		//Check if we need to "save" files
		Parameters params = data.getParameters();
		<#list fileItems as item>
		repository.store(${meta.referenceName}, params.getFileItem("${meta.referenceName}.${item.name}"), "${item.name}", ${meta.referenceName}.${item.getter}(), true);
		</#list>
    }
	</#if>
}