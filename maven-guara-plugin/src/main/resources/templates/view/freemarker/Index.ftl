<h2>${meta.shortDomainClass} Page:index</h2>
<ul>
	<li>
		<#assign url = '{link.page("${meta.referenceName}.List").action("${meta.shortDomainClass}Control").exec("list")}' />
		<a href="$${url}">list all ${meta.shortDomainClass}</a>
	</li>
	<li>
		<#assign url = '{link.page("${meta.referenceName}.Edit")}' />
		<a href="$${url}">new ${meta.shortDomainClass}</a>
	</li>

</ul>