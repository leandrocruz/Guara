package ${meta.packageName};

import br.com.ibnetwork.guara.app.xingu.pull.BeanTool;
import ${meta.domainClass};

public class ${meta.compilationUnit}
    extends BeanTool
{

	@Override
    protected Class<${meta.shortDomainClass}> getBeanClass()
    {
	    return ${meta.shortDomainClass}.class;
    }

}
