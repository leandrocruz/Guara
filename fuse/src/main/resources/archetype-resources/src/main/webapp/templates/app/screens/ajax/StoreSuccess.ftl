<#list storeResults as result>
	<#assign meta = result.beanInfo />
	<#assign bean = result.bean />
	dojo.byId("${result.beanName}.id").value="${bean.id}";
	<#list meta.inputFields as property>
		<#assign fullName = result.beanName + "." + property.name/>
		<#if property.inputType == "select">
			<#assign value = referenceTool.loadOption(property.referenceLoader,property.value?string).display />
			Guara.updateStaticValue('${fullName}:static','${value}');
		<#elseif property.inputType == "file">
			preview = dojo.byId("${fullName}:fileSrc");
			if(preview)
			{
				<#assign value = meta.getValueFormatted(property.name) />
				<#assign url   = repository.getTargetDir(bean,property.name)+"/"+value/>
				preview.src = "${content.getURI(url)}";
			}
		<#else>
			<#assign value = meta.getValueFormatted(property.name) />
			Guara.updateStaticValue('${fullName}:static','${value?replace('\n','<br>')?js_string}');
		</#if>
		Guara.removeAllMarkers('${fullName}');
	</#list>
	Guara.enableEditInPlace('${result.beanName}Table');
</#list>

