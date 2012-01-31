<h2>${meta.shortDomainClass} Page:show</h2>
${r"<#assign"} id = parameters.getLong("id")${r"/>"}
${r"<#assign"} ${meta.referenceName} = ${meta.referenceName}Tool.getById(id)/>

<table cellPadding="4" cellSpacing="0" class="guara nameValue">
	<thead>
		<tr>
			<th colSpan="2">
				${r"${"}${meta.referenceName}${r".display}"}
			</th>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<td colSpan="2">
				<#assign url = '{link.page("${meta.referenceName}.Edit").add("id",${meta.referenceName}.id)}' />
				<a href="$${url}">edit</a> |
				<#assign url = '{link.page("${meta.referenceName}.List").action("${meta.shortDomainClass}Control").exec("list")}' />
				<a href="$${url}">list</a> |
				<#assign url = '{link.page("${meta.referenceName}.List").action("${meta.shortDomainClass}Control").exec("delete").add("id",${meta.referenceName}.id)}' />
				<a href="$${url}">delete</a>			
			</td>
		</tr>
	</tfoot>
	<tbody>
		<#list meta.beanInfo.inputFields as property>
		<tr>
			<td>${property.fieldLabel}</td>
			<#assign exp = '{${meta.referenceName}.${property.name}!}' />
			<#if property.format??>
				<#assign exp = '{${meta.referenceName}.${property.name}!?string("${property.format}")}'>
			</#if>
			<td>$${exp}</td>
		</tr>
		</#list>
	</tbody>
</table>

