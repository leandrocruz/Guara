<h2>Cadastro ${meta.shortDomainClass}</h2>
<#assign beanInfo = meta.referenceName + "Meta"/>
<#assign toolName = meta.referenceName + "Tool"/>
<#assign ref = meta.referenceName/>

${r"<#if"} !${meta.referenceName}${r"?exists>"}
	${r'<#assign id = parameters.getLong("id")/>'}
	${r"<#if (id > 0)>"}
		${r"<#assign"} ${meta.referenceName} = ${meta.referenceName}${r"Tool.getById(id)/>"}	
	${r"</#if>"}
${r"</#if>"}

<#noparse>
<#if pageInfo.actionName?? >
    <#assign action = pageInfo.actionName />  
	<#assign methodName = "doPerform"/>
	<#assign templateName = ""/>
<#else>
</#noparse>
	<!-- CRUD -->
    ${r'<#assign action  = "'}${meta.shortDomainClass}Control${r'"/>'}
	${r'<#assign methodName = "store"/>'}
	${r'<#assign templateName = "'}${meta.referenceName}.Show${r'"/>'}
${r'</#if>'}
<#assign url = '{link.page(templateName).action(action).exec(methodName)}' />
<form 	id="${ref}Form
		action="$${url}" 
		method="POST"
		enctype="${meta.beanInfo.formEncType}">
<#assign display = '{${meta.referenceName}.display}'>
<table cellPadding="4" cellSpacing="0" class="guara nameValue">
	<thead>
		<tr>
			<th colSpan="2">
				${r'<#if (id > 0)>'}
					$${display}
				${r'<#else>'}
					Create new ${meta.shortDomainClass}
				${r'</#if>'}
			</th>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<td colSpan="2">
				${r'<@g.flowbar labelNext="Proximo" labelBack="Anterior"/>'}
				<#assign url = '{link.page("${meta.referenceName}.List").action("${meta.shortDomainClass}Control").exec("list")}' />
				<a href="$${url}">list</a> 
				${r'<#if (id > 0) >'}
					<#assign url = '{link.page("${meta.referenceName}.Show").action("${meta.shortDomainClass}Control").exec("delete").add("id",${meta.referenceName}.id)}' />
					| <a href="$${url}">delete</a>			
				${r'</#if>'}
			</td>
		</tr>
	</tfoot>
	<tbody>
		<tr>
			<td>Id</td>
			<td>
				<#assign value = '{${meta.referenceName}.id!}' />
				$${value}
				<input type="hidden" name="${meta.referenceName}.id" value="$${value}">
			</td>
		</tr>
<#list meta.beanInfo.inputFields as property>
	<#if !property.isPrimaryKey()>
		<tr>
			<td>${property.fieldLabel}${property.isRequired()?string("*","")}</td>
			<#assign fieldValue = '{${meta.referenceName}.${property.name}!}' />
			<#assign fieldName  = '${meta.referenceName}.${property.name}'/>
			<#if property.format??>
				<#assign fieldValue = '{${meta.referenceName}.${property.name}!?string("${property.format}")}'/>
				<#assign fieldName  = '${meta.referenceName}.${property.name}::${property.format}'/>
			</#if>
			<td>
				
		<#if property.widgetType?? >
			<#assign switchOn = property.widgetType />
		<#else>
			<#assign switchOn = property.inputType />
		</#if>
		<#--
			property: ${property.name}
			propertyType: ${property.inputType}
			widget: ${property.widgetType!}
			switchOn: ${switchOn}
		-->
		<#switch switchOn >
			<#case "text">
			<#case "hidden">
			<#case "password">
				<input type="${property.inputType}" name="${fieldName}" value="$${fieldValue}" size="${property.size!""}" maxLength="${property.maxLength!""}">
			<#break>
			<#case "textArea">
				<textarea name="${fieldName}" rows="${meta.referenceName}.${property.rows!"5"}" cols="${property.cols!"30"}">$${fieldValue}</textarea>
			<#break>
			<#case "select">
				<#assign collection = "<#assign options = referenceLoader.loadReferences(\"${property.referenceLoader}\")/>"/>
				${collection}
				<select name="${fieldName}">
					${r'<#list options as option>'}
						<option value="${r'${option.value}'}">${r'${option.display}'}</option>
					${r'</#list>'}
				</select>
			<#break>
			<#case "dojo:DropdownDatePicker">
				<#assign rfc3339 = '{${meta.referenceName}.${property.name}!?string("${property.rfc3339}")}'/>
				<div 	dojoType="DropdownDatePicker" 
						name="${fieldName}" 
						value="$${rfc3339!}" 
						saveFormat="${property.format}"
						displayFormat="${property.format}"></div>	
			<#break>
			<#default>
				WARNING: Can't handle ${property.inputType} for ${property.name}.						
		</#switch>
			</td>
		</tr>
	</#if>
</#list>			
	</tbody>
</table>
</form>


