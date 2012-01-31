<#assign beanInfo = meta.referenceName + "Meta"/>
<#assign toolName = meta.referenceName + "Tool"/>
<#assign ref = meta.referenceName/>
${r'<#macro'} ${ref}Macro id meta template="" action="" methodName="" pipeline="" head="" foot="" validator={"errorCount":0}>
	<#assign url = '{link.pipeline(pipeline).page(template).action(action).exec(methodName)}' />
	<form	id="${r'${meta.beanName}'}Form"
			method="POST"
			action="$${url}"
			enctype="${r'${meta.formEncType}'}">
		<input type="hidden" id="${r'${meta.beanName}'}.id" name="${r'${meta.beanName}'}.id" value="${r'${id}'}">
	<table id="${r'${meta.beanName}'}Table" cellPadding="4" cellSpacing="0" class="guara nameValue">
		<thead>
			<tr>
				<th colSpan="2">
					${r'<#if'} head?has_content>
						${r'${head}'}
					${r'<#else>'}
						${r'<#if (id > 0) >'}
							${r'${bean.display}'}
						${r'<#else>'}
							Cadastre ${meta.shortDomainClass}
						${r'</#if>'}
					${r'</#if>'}						
				</th>
			</tr>
			<#noparse>
			<#if (validator.errorCount > 0) >
				<tr>
					<th colSpan="2" class="error">
						<a href="javascript:dojo.widget.byId('${r'${meta.beanName}'}ErrorDialog').show()">mostrar erros</a>				
					</th>
				</tr>
			</#if>
			</#noparse>
		</thead>
		<tfoot>
			<tr>
				<td colSpan="2">
					${r'<#if'} foot?has_content>
						${r'${foot}'}
					${r'<#else>'}
						<#assign url = '{link.page("${ref}.List").action("${meta.shortDomainClass}Control").exec("list")}' />
						<a href="$${url}">listar</a> 
						${r'<#if (id > 0) >'}
							| 
							<a href="#" onClick="Guara.enableEditInPlace('${r'${meta.beanName}'}Table')">editar</a>
							<input class="guaraShow" type="button" value="confirmar" onClick="Guara.submitForm('${r'${meta.beanName}'}Form')">
							| 
							<#assign url = '{link.page("${ref}.List").action("${meta.shortDomainClass}Control").exec("delete").add("id",id)}' />
							<a href="$${url}">remover</a>			
						${r'<#else>'}
							<input type="button" value="confirmar" onClick="Guara.submitForm('${r'${meta.beanName}'}Form')">
						${r'</#if>'}
					${r'</#if>'}
				</td>
			</tr>
		</tfoot>
		<tbody>
	<#assign currentGroup = ""/>
	<#list meta.beanInfo.inputFields as property>
		<#if currentGroup != property.groupName>
			<#assign currentGroup = property.groupName/>
			<tr>
				<td class="groupSeparator">${currentGroup}</td>
				<td class="groupSeparator"></td>
			</tr>
		</#if>
		<#--
		<#assign fieldValue = "{(bean.${property.name})!?string}" />
		<#assign fieldName  = "${r'${meta.beanName}'}.${property.name}"/>
		<#if property.format??>
			<#assign fieldValue = '{(bean.${property.name})!?string("${property.format}")}'/>
			<#assign fieldName  = "${r'${meta.beanName}'}.${property.name}::${property.format}"/>
		</#if>
		<#if property.inputType == "select">
			<#assign fieldValue = '{referenceTool.loadOption("${property.referenceLoader}","$${fieldValue}").display}' />
		<#else>
			<#assign fieldValue = "{meta.getValueFormatted(\"${property.name}\")}" />
			<#assign fieldValueReplaced = "{meta.getValueFormatted(\"${property.name}\")?replace('\\n','<br>')}" />
		</#if>
		-->
		<#if property.includeOnDetail()>
			<#assign domId = "{beanName}." + property.name />
			<#assign macroName = "@g.showField"/>
			<${macroName} meta=meta fieldName="${property.name}"		mode="table" />
			<#--
			<tr class="guaraShow">
				<td id="$${domId}:staticLabel">${property.fieldLabel}</td>
				<td id="$${domId}:static">
				<#if "file" == property.inputType>
					<#assign macroName = "@g.toolTip"/>
					<${macroName} connectId="$${domId}:filePreview" label="preview" url="#">
						${r"<#if"} ${ref}??>
							${r'<#assign'} fileName = meta.getValueFormatted("${property.name}")/>
							${r'<#assign'} url = content.getURI(repository.getTargetDir(${ref},"${property.name}")+"/"+fileName)/>
						${r"</#if>"}
						<img id="$${domId}:fileSrc" ${r'<#if url?? >src="${url}"</#if>'}>
					</${macroName}>
				<#elseif "textarea" == property.inputType>
					$${fieldValueReplaced}
				<#else>
					$${fieldValue}
				</#if>
				</td>
			</tr>
			<tr class="guaraEdit">
				<td id="$${domId}:label">
					${property.fieldLabel}${property.isRequired()?string("*","")}
					<#assign macroName = "@g.toolTip"/>
					<${macroName} label="?" 
								connectId="$${domId}:errorTooltip" 
								anchorClass="errorTooltip"></${macroName}>
				</td>
				<td>
			<#if property.widgetType?? >
				<#assign switchOn = property.widgetType />
			<#else>
				<#assign switchOn = property.inputType />
			</#if>
			<#switch switchOn >
				<#case "text">
				<#case "hidden">
				<#case "password">
				<#case "file">
					<input 	id="$${domId}"
							name="${fieldName}" 
							type="${property.inputType}"  
							value="$${fieldValue}" 
							size="${property.size!""}" 
							maxLength="${property.maxLength!""}"
							guaraStatus=""/>
				<#break>
				<#case "textarea">
					<textarea	id="$${domId}"
								name="${fieldName}" 
								rows="${property.rows!"5"}" 
								cols="${property.cols!"30"}"
								guaraStatus="">$${fieldValue}</textarea>
				<#break>
				<#case "select">
				<#case "checkbox">
				<#case "radio">
					<#assign fieldValue = '{(bean.${property.name})!?string}' />
					<#assign macroName = "@g.combo"/>
					<${macroName}	id="$${domId}" 
								name="${fieldName}"
								type="${property.inputType}" 
								options=referenceTool.load("${property.referenceLoader}")
								selectedOption="$${fieldValue}"/>
				<#break>
				<#case "dojo:DropdownDatePicker">
					<div 	id="$${domId}"
							dojoType="DropdownDatePicker" 
							name="${fieldName}" 
							value="$${fieldValue}" 
							saveFormat="${property.format}"
							displayFormat="${property.format}"
							guaraStatus=""></div>	
				<#break>
				<#default>
					WARNING: Can't handle ${property.inputType} for ${property.name}.						
			</#switch>
				</td>
			</tr>
			-->
		</#if>
		</#list>
		</tbody>
	</table>
	</form>
	<script>
		<#noparse>
		<#if meta.formEncType == "multipart/form-data">
			dojo.require("dojo.io.IframeIO"); //used for file uploads
		</#if>
		</#noparse>
		<#list meta.beanInfo.inputFields as property>	
			<#if property.includeOnDetail()>
				<#assign domId = "${r'${meta.beanName}'}." + property.name />
				<#switch property.inputType >
					<#case "text">
					<#case "hidden">
					<#case "password">
					<#case "file">
					<#case "select">
		dojo.event.connect(dojo.byId("${domId}"),"onchange",function(e){Guara.addDirtyMarker(e,"${domId}")});
					<#break>	
				</#switch>
			</#if>
		</#list>
		<#noparse>
		<#if (id == 0) >
			Guara.enableEditInPlace('${meta.beanName}Table');
		</#if>
		<#if (validator.errorCount > 0) >
			<#list validator.fields as fieldName>
				<#assign name = beanName + "." + fieldName/>
				<#assign result = validator.get(fieldName)/>
				<#if !result.valid >
					Guara.addErrorMarker('${name}');
				</#if>
			</#list>
		</#if>
		</#noparse>
	</script>
	
	<#noparse>
	<#if (validator.errorCount > 0) >
		<@g.modal id="${beanName}ErrorDialog" style="width: 400px;">
			<table class="error" cellPadding="4" cellSpacing="0">
			<#list validator.fields as fieldName>
				<#assign result = validator.get(fieldName)/>
				<#if !result.valid >
					<tr>
						<td>${meta.getInputField(fieldName).fieldLabel}</td>
						<td>${result.validator.message}</td>
					</tr>
				</#if>
			</#list>
			</table>
		</@g.modal>
	</#if>
	</#noparse>
${r'</#macro>'}