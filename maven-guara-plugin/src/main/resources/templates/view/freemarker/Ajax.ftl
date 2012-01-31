<#assign beanInfo = meta.referenceName + "Meta"/>
<#assign toolName = meta.referenceName + "Tool"/>
<#assign ref = meta.referenceName/>

<h2>Cadastro ${meta.shortDomainClass}</h2>

${r"<#if"} !${ref}${r"?exists>"}
	${r'<#assign id = parameters.getLong("id")/>'}
	${r"<#if (id > 0)>"}
		${r"<#assign"} ${ref} = beanTool.getById("${meta.modelName}",id)/>
		${r'<#assign'} ${beanInfo} = beanTool.getBeanInfo(${ref})/>
	${r"<#else>"}
		${r'<#assign'} ${beanInfo} = beanTool.getBeanInfo("${meta.modelName}")/>
	${r"</#if>"}
${r"<#else>"}
	${r'<#assign id = '}${ref}.id/>
	${r'<#assign'} ${beanInfo} = beanTool.getBeanInfo(${ref})/>
${r"</#if>"}

<#noparse>
<#if pageInfo.actionName?? >
    <#assign action = pageInfo.actionName />  
	<#assign methodName = ""/>
	<#assign pipeline = ""/>
<#else>
</#noparse>
	<!-- CRUD -->
    ${r'<#assign'} action  = "${meta.shortDomainClass}Control"/>
	${r'<#assign'} methodName = "store"/>
	${r'<#assign'} pipeline = "ajax"/>
${r'</#if>'}

<#assign macroName = "@g.beanMacro"/>
<${macroName} 	id = id 
				meta = ${beanInfo}
				action = action 
				methodName = methodName 
				pipeline = pipeline />