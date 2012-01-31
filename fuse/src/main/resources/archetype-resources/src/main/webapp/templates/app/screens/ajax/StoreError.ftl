<#list storeResults as result>
	<#assign vCtx = result.validatorContext />
	<#list vCtx.fields as fieldName>
		<#assign field = vCtx.get(fieldName) />
		<#if (result.index >= 0) >
			<#assign name = result.beanName +"["+result.index+"]." + fieldName/>
		<#else>
			<#assign name = result.beanName + "." + fieldName/>
		</#if>
		<#if field.valid >
			Guara.removeErrorMarker('${name}');
		<#else>
			<#assign errorMessage = field.validator.message/>
			Guara.addErrorMarker('${name}','${errorMessage}');
		</#if>
	</#list>
</#list>

<#--
<#list storeResults as result>
	<#assign vCtx = result.validatorContext />
	<#if (vCtx.errorCount > 0) >
		var table = document.createElement('table');
		dojo.html.addClass(table, "error");
		var overlay = null;
		var messages = dojo.byId('messages');
		<#list vCtx.fields as fieldName>
			<#assign name = result.beanName + "." + fieldName/>
			<#assign field = vCtx.get(fieldName)/>
			<#if !field.valid >
				if(messages)
				{
					var tr = document.createElement('tr');
					var td = document.createElement('td');
					var txt = document.createTextNode('{guaraBeanInfo.getInputField(fieldName).fieldLabel}');
					td.appendChild(txt);
					tr.appendChild(td);
					td = document.createElement('td');
					<#assign errorMessage = field.validator.message/>
					txt = document.createTextNode('${errorMessage}');
					td.appendChild(txt);
					tr.appendChild(td);
					table.appendChild(tr);
				}
				overlay = dojo.byId('${name}:errorTooltip:overlay');
				if(overlay)
				{
					overlay.innerHTML = "${errorMessage}";
				}
			<#else>
				overlay = null;
			</#if>
		</#list>
		if(messages)
		{
			Guara.loadModalWindow('messages',null);
			dojo.byId('messages:content').appendChild(table);
		}
	</#if>
</#list>
-->