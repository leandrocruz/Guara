<#macro showMessage errors={}>
	<#if data.hasMessage() >
		<#assign messageType = data.message.type />
		<#if messageType == "error" >
			<#assign divId = "errorMessageLayer" />
			<#assign color = "red" />
			<#assign label = "erro" />
		<#elseif messageType == "warning" >
			<#assign divId = "errorMessageLayer"/>
			<#assign color = "#FF9933" />
			<#assign label = "aviso" />
		<#else>
			<#assign divId = "messageLayer" />
			<#assign color = "#444" />
			<#assign label = "info" />
		</#if>
		<div id="${divId}">
			<#if (errors?keys?size > 0) >
				<a 	onClick="Esqueleto.showModalWindow('formMessage');" 
					style="color: ${color}">[${label}]</a>
			<#else>
				<font style="color: ${color}">[${label}]</font>
			</#if>
			&nbsp;${data.messageAsText}
		</div>
		<#if (errors?keys?size > 0) >
			<@app.modal id="formMessage" title="Erros no formul�rio" style="width: 500px;">
				<@intake.showErrors errors=errors />
			</@app.modal>
			<script>
				Esqueleto.showModalWindow('formMessage');
			</script>
		</#if>
	</#if>
</#macro>

<#macro modal id style="" href="" showHeader=true header="">
	<div 	dojoType="Dialog" 
			id="${id}"
			style="display: none;">
		<div  class="guaraDialog" style="${style}">
			<#if showHeader>
				<#if header?has_content>
					${header}
				<#else>
					<div class="guaraDialogHead">
						<a id="${id}DialogClose" href="javascript:${id}Dialog.hide()">fechar<a>
					</div>
				</#if>
			</#if>
	<div 	id="${id}:content"
					class="guaraDialogBody"
					dojoType="ContentPane" 
					href="${href}"
					executeScripts="true"
					parseContent="true"
					refreshOnShow="false"><#nested></div>
		</div>
	</div>
	<script language="JavaScript">
		dojo.addOnLoad(function(){${id}Dialog = dojo.widget.byId("${id}");});
	</script>
</#macro>

<#macro modal2 id type="info" title="" menuEnabled=true style="" useContentPane=false href="">
	<#if useContentPane>
		<#assign dojoType = "ContentPane">
		<script type="text/javascript">
			dojo.require("dojo.widget.ContentPane");
		</script>
	<#else>
		<#assign dojoType = "">
	</#if>
	<div 	id="${id}:pane" class="modalPane">
		<#if menuEnabled>
			<div id="${id}:menu" class="modalMenu">
				<#if title?has_content>
					<p class="modalMenuTitle">${title}</p> | 
				</#if>
				<a id="closeModalMenu" onClick="Esqueleto.closeModalWindow()">fechar</a>
			</div>
		<#elseif title?has_content>
			<div id="${id}:menu" class="modalMenu">
				<p class="modalMenuTitle">${title}</p>
			</div>
		</#if>
		<div 	id="${id}" 
				class="modalContent" 
				style="${style}"
				dojoType="${dojoType}" 
				href="${href}"
				executeScripts="true"
				parseContent="true"
				refreshOnShow="false"><#nested></div>
	</div>
</#macro>

<#macro toolTip connectId label="tooltip" url="#" target="" domEvents={} class="" anchorClass="" style="" tabIndex="0">
	<div 	id="${connectId}:overlay"
			dojoType="tooltip" 
			connectId="${connectId}" 
			toggle="fade"
			class="${class}"
			style="${style}"><#nested></div>
	<a id="${connectId}" href="${url}" tabIndex="${tabIndex}" target="${target}" class="${anchorClass}"
		<#list domEvents?keys as eventName>
			${eventName}="${domEvents[eventName]}"
		</#list>
	>${label}</a>
</#macro>

<#macro combo id name options selectedOption="" selectedOptions=[] type="select" maxOptionsSameLine=2>
	<#switch type>
		<#case "select">
			<select id="${id}" name="${name}" guaraStatus="">
				<#list options as op>
					<#assign selected = "">
					<#assign disabled = "DISABLED">
					<#if op.value?? >
						<#assign disabled = "">
						<#if selectedOption == op.value >
							<#assign selected = "SELECTED">
						</#if>
					</#if>
					<option value="${op.value!""}" ${disabled} ${selected}>${op.displayInCombo}</option>
				</#list>
			</select>
		<#break>
		<#case "checkbox">
		<#case "radio">
			<#list options as op>
				<#assign selected = "">
				<#if op.value?? >
					<#if (selectedOptions?size > 0)>
						<#list selectedOptions as v>
							<#if v?string == op.value >
								<#assign selected = "CHECKED">
								<#break>
							</#if>
						</#list>
					<#else>
						<#if selectedOption == op.value >
							<#assign selected = "CHECKED">
						</#if>
					</#if>
					<input type="${type}" id="${name}:${op.value}" name="${name}" value="${op.value}" ${selected}/> ${op.displayInCombo} 
					<#if (options?size > maxOptionsSameLine) ><br></#if>
				</#if>
			</#list>
		<#break>
	</#switch>
</#macro>

<#macro showField meta fieldName index=-1 size=0 maxLength=0 styleLabel="" styleInput="" mode="loose" enableEditInPlace=true>
	<#switch mode>
		<#case "table">
			<#if enableEditInPlace >
				<tr class="guaraShow">
					<td>
						<@g.fieldLabelStatic	fieldName=fieldName meta=meta style=styleLabel index=index/>				
					</td>
					<td>
						<@g.fieldValue			fieldName=fieldName meta=meta style=styleInput index=index/>
					</td>
				</tr>
			</#if>
			<tr class="guaraEdit">
				<td>
					<@g.fieldLabel 	fieldName=fieldName meta=meta style=styleLabel index=index/>				
				</td>
				<td>
					<@g.field		fieldName=fieldName meta=meta style=styleInput size=size maxLength=maxLength index=index />
				</td>
			</tr>
		<#break>
		<#case "loose">
			<@g.fieldLabel 	fieldName=fieldName meta=meta style=styleLabel index=index/>
			<@g.field		fieldName=fieldName meta=meta style=styleInput size=size maxLength=maxLength index=index/>
		<#break>
		<#default>
			WARNING: Can't handle mode: ${mode}.
	</#switch>
</#macro>

<#function generateDomId meta fieldName index=-1>
	<#if (index >= 0) >
		<#assign domId = meta.beanName+"["+index+"]."+fieldName />
	<#else>
		<#assign domId = meta.beanName+"."+fieldName />
	</#if>
	<#return domId />
</#function>

<#macro fieldLabelStatic meta fieldName index=-1 style=""> 
	<#assign property = meta.getInputField(fieldName) />
	<#assign domId = generateDomId(meta, fieldName, index) />
	<span id="${domId}:staticLabel" style="${style}">
		${property.fieldLabel}<#if property.isRequired()>*</#if>
	</span>
</#macro>

<#macro fieldLabel meta fieldName index=-1 style=""> 
	<#assign property = meta.getInputField(fieldName) />
	<#if property.inputType != "hidden" >
		<#assign domId = generateDomId(meta, fieldName, index) />
		<div id="${domId}:label" style="${style}" class="fieldLabel">
			${property.fieldLabel}<#if property.isRequired()>*</#if>
			<@g.toolTip label="?"
						connectId="${domId}:errorTooltip" 
						anchorClass="errorTooltip"
						tabIndex="-1"></@g.toolTip>
		</div>
	</#if>
</#macro>

<#macro fieldValue meta fieldName index=-1 style="">
	<#assign property = meta.getInputField(fieldName) />
	<#assign domId = generateDomId(meta, fieldName, index) />
	<span id="${domId}:static" class="fieldValue">
		${meta.getValueFormatted(fieldName)!""}
	</span>
</#macro>

<#macro connectEvent targetId event action >
	<event	id=""
			targetId="${targetId}"
			type="${event.type()?lower_case}"
			runAt="${event.runAt()?lower_case}"
			action="${action}"/>
	<script>
		<#-- dojo.event.connect(dojo.byId("${targetId}"),"on${event.type()?lower_case}",function(e){alert(e.target+':'+e.type);}); -->
		dojo.event.connect(dojo.byId("${targetId}"),"on${event.type()?lower_case}","react");
	</script>
</#macro>

<#macro field meta fieldName index=-1 style="" size=0 maxLength=0 >
	<#assign property = meta.getInputField(fieldName) />
	<#assign domId = generateDomId(meta, fieldName, index) />
	<#assign eventsToFire = [] />
	<#if property.widgetType?? >
		<#assign switchOn = property.widgetType />
	<#else>
		<#assign switchOn = property.inputType />
	</#if>
	<div id="${domId}:field" <#if property.inputType != "hidden" >style="${style}" class="field"</#if>>
		<#switch switchOn >
			<#case "text">
			<#case "hidden">
			<#case "password">
			<#case "file">
			<#case "iMask">
				<input 	id="${domId}"
						<#if switchOn == "iMask">
							class="iMask"
							alt="${property.mask!""}"
							name="${domId}::${property.format}"
						<#else>
							name="${domId}"
						</#if>
						type="${property.inputType}"  
						value="${meta.getValueFormatted(fieldName)!""}" 
						size="<#if (size > 0) >${size}<#else>${property.size!""}</#if>"
						maxLength="
							<#if (maxLength > 0) >${maxLength}<#else><#if (size > 0) >${size}<#else>${property.maxLength!""}</#if></#if>"
						guaraStatus=""/>
			<#break>
			<#case "textarea">
				<textarea	id="${domId}"
							name="${domId}" 
							rows="${property.rows!"5"}" 
							cols="${property.cols!"30"}"
							guaraStatus="">${meta.getValueFormatted(fieldName)!""}</textarea>
			<#break>
			<#case "select">
			<#case "checkbox">
			<#case "radio">
				<#assign loader = referenceTool.getLoader("${property.referenceLoader}") />
				<#assign selected = (loader.defaultOption.value)!"0" />
				<#if selected != "0" && !meta.hasPrimaryKey() && switchOn == "select">
					<#assign script>
						dojo.addOnLoad(function () {
							dojo.debug("firing event onChange on: ${domId}");
							var evt = document.createEvent("HTMLEvents");
							evt.initEvent("change", false, false);
							dojo.byId('${domId}').dispatchEvent(evt);
						});
					</#assign>
					<#assign eventsToFire = eventsToFire + [script] />
				</#if>
				<#if property.value?? && (meta.bean.id > 0)>
					<#if property.isCollection() >
						<#assign selectedOptions = property.value />
						<#assign selected = "" />
					<#else>
						<#assign selected = property.value?string />
						<#assign selectedOptions = [] />						
					</#if>
				</#if>
				<br>
				<@g.combo 	id="${domId}" 
					name="${domId}"
					type="${property.inputType}" 
					options=loader.loadOptions(selected)![]
					selectedOption=selected
					selectedOptions=selectedOptions />				
			<#break>
			<#case "dojo:DropdownDatePicker">
				<div 	id="${domId}"
						dojoType="DropdownDatePicker" 
						name="${domId}::${property.format}" 
						value="${meta.getValueFormatted(fieldName)}"
						saveFormat="${property.format}"
						displayFormat="${property.format}"
						guaraStatus=""></div>
			<#break>
			<#default>
				WARNING: Can't handle ${property.inputType} for ${property.name}.						
		</#switch>
		<events id="${domId}:events">
			<#assign events = property.events />
			<#list events as event >
				<#if "CLIENT" == event.runAt() >
					<#assign action = event.action() />
				<#elseif "SERVER" == event.runAt() >
					<#assign action = link.pipeline("ajax").action(event.action()).exec(event.method()) />
				</#if>
				<#if property.inputType == "checkbox" >
					<#list loader.loadOptions(selected)![] as op>
						<@g.connectEvent targetId="${domId}:${op.value}" event=event action=action />
					</#list>		
				<#else>
					<@connectEvent targetId=domId event=event action=action />
				</#if>
			</#list>
		</events>
		<#if (eventsToFire?size > 0) >
			<script>
				<#-- must execute after events are connected. See <events> abobe -->
				<#list eventsToFire as script>
					${script}
				</#list>
			</script>
		</#if>
		<#if property.includeOnDetail() >
			<#switch property.inputType >
				<#case "text">
				<#case "hidden">
				<#case "password">
				<#case "file">
				<#case "select">
					<script>
						dojo.event.connect(dojo.byId("${domId}"),"onchange",function(e){Guara.addDirtyMarker(e,"${domId}")});
					</script>
				<#break>	
			</#switch>
		</#if>
	</div>
</#macro>

<#macro beanMacro id meta template="" action="" methodName="" pipeline="" head="" foot="" validator={"errorCount":0}>
	<#assign beanName = meta.beanName />
	<form	id="${beanName}Form"
			method="POST"
			action="${link.pipeline(pipeline).page(template).action(action).exec(methodName)}"
			enctype="${meta.formEncType}">
		<input type="hidden" id="${beanName}.id" name="${beanName}.id" value="${id}">
	<table id="${beanName}Table" cellPadding="4" cellSpacing="0" class="guara nameValue">
		<thead>
			<tr>
				<th colSpan="2">
					<#if head?has_content>
						${head}
					<#else>
						<#if (id > 0) >
						<#else>
							Cadastre ${beanName}
						</#if>
					</#if>						
				</th>
			</tr>
			<#if (validator.errorCount > 0) >
				<tr>
					<th colSpan="2" class="error">
						<a href="javascript:dojo.widget.byId('${beanName}ErrorDialog').show()">mostrar erros</a>				
					</th>
				</tr>
			</#if>
		</thead>
		<tfoot>
			<tr>
				<td colSpan="2">
					<#if foot?has_content>
						${foot}
					<#else>
						<a href="${link.page("admin.guara.${beanName}.List")}">listar</a> 
						<#if (id > 0) >
							| 
							<a href="#" onClick="Guara.enableEditInPlace('${beanName}Table')">editar</a>
							<input class="guaraShow" type="button" value="confirmar" onClick="Guara.submitForm('${beanName}Form')">
							| 
							<a href="${link.page("admin.guara.${beanName}.List").action(action).exec("delete").add("id",id)}">remover</a>			
						<#else>
							<input type="button" value="confirmar" onClick="Guara.submitForm('${beanName}Form')">
						</#if>
					</#if>
				</td>
			</tr>
		</tfoot>
		<#--
			*
			* Show all fields
			*	
		-->
		<tbody>
			<#list meta.inputFields as property >	
				<#if property.includeOnDetail() >
					<@g.showField meta=meta fieldName=property.name mode="table" />
				</#if>
			</#list>
		</tbody>
	</table>
	</form>
	<script>
		<#if meta.formEncType == "multipart/form-data">
			dojo.require("dojo.io.IframeIO"); //used for file uploads
		</#if>
		
		<#--
			*
			* Use dojo.event.connect to mark fields as dirty
			*	
		<#list meta.inputFields as property >	
			<#if property.includeOnDetail() >
				<#assign domId = generateDomId(meta, property.name, index) />
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
		-->
		
		<#--
			*
			* enable edit in place if creating a new bean
			*	
		-->
		<#if (id == 0) >
			Guara.enableEditInPlace('${beanName}Table');
		</#if>

		<#--
			*
			* Add error markers if got any errors
			*	
		-->
		<#if (validator.errorCount > 0) >
			<#list validator.fields as fieldName>
				<#assign name = beanName + "." + fieldName/>
				<#assign result = validator.get(fieldName)/>
				<#if !result.valid >
					Guara.addErrorMarker('${name}');
				</#if>
			</#list>
		</#if>
	</script>
	
	<#--
		*
		* Create error table if got any errors
		*	
	-->
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
</#macro>

<#macro bind meta index=-1 useHiddenNodes=true disable=false>
	var node = null;
	var cssClass = "bindLabel";
	<#assign beanName = meta.beanName />
	<#list meta.inputFields as property >	
		<#assign fieldName = property.name />
		<#if property.includeOnDetail() >
			<#assign domId = generateDomId(meta, fieldName, index) />
			node = dojo.byId('${domId}');
			if(node) {
				Guara.removeAllMarkers(node.id);
				Guara.setValue(node, '${property.valueFormatted}');
				<#if useHiddenNodes >
					var labelText = "LABEL GOES HERE";
					var label = document.createElement("span");
					dojo.html.setClass(label,cssClass);
					var parent = node.parentNode;
					switch(node.tagName)
					{
						case "INPUT":
							node.type = "hidden";
							labelText = "${property.valueFormatted}";
							break;
						case "SELECT":
							var replacement = document.createElement("input");
							replacement.id = node.id;
							replacement.type = "hidden";
							replacement.name = node.name;
							replacement.value = node[node.selectedIndex].value;
							labelText = node[node.selectedIndex].text;
							dojo.dom.replaceNode(node, replacement);
							break;
						default:
					}
					label.appendChild(document.createTextNode(labelText));
					parent.appendChild(label);
				</#if>
				<#if disable >
					node.disabled = true;
				<#else>
					node.disabled = false;
				</#if>
			}
		</#if>
	</#list>
</#macro>
