<h2>${meta.shortDomainClass} Page:list</h2>

${r"<#if !list?exists>"}
	${r"<#assign"} list = beanTool.getAll("${meta.modelName}") ${r"/>"}
${r"</#if>"}
<#assign columnCount = meta.beanInfo.properties?size />
${r"<#assign columnCount = "}${columnCount}${r" />"}

<table cellPadding="4" cellSpacing="0" class="guara dataGrid">
	<tHead>
		<tr>
			<#list meta.beanInfo.inputFields as property>
				<#if property.includeOnListing()>
			<th>${property.fieldLabel}</th>
				</#if>
			</#list>
		</tr>
	</tHead>
	<tFoot>
		<tr>
			<td colSpan="${r"${columnCount}"}">found <b>${r"${list?size}"} ${meta.referenceName}(s)</b></td>
		</tr>
		<tr>
			<td colSpan="${r"${columnCount}"}">
				<#assign url = '{link.page("admin.guara.${meta.referenceName}.Ajax")}' />
				<a href="$${url}">novo ${meta.shortDomainClass}</a> |
				<#assign url = '{link.page("admin.guara.${meta.referenceName}.Index")}' />
				<a href="$${url}">${meta.shortDomainClass} home</a>
			</td>
		</tr>
	</tFoot>
	<tBody>
		${r"<#list list as"} ${meta.referenceName} ${r">"}
			<tr>
				<#list meta.beanInfo.inputFields as property>
					<#if property.includeOnListing()>
						<#assign exp = '{${meta.referenceName}.${property.name}' + property.nullable?string("!","") + '?string}'>
						<#if property.format??>
							<#assign exp = '{${meta.referenceName}.${property.name}' + property.nullable?string("!","") + '?string("${property.format}")}'>						
						</#if>
						<#if property.name == "id">
							<#assign url = '{link.page("admin.guara.${meta.referenceName}.Ajax").add("id",${meta.referenceName}.id)}' />
				<td><a href="$${url}">$${exp}</a></td>
						<#else>
				<td>$${exp}</td>
						</#if>
					</#if>
				</#list>
			</tr>
		${r"</#list>"}	
	</tBody>
</table>

