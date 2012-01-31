<html>
	<head>
		<title>System Error</title>
		<link href="${content.getURI("/resources/css/guara.css")}" 		type="text/css" rel="stylesheet"/> 
	</head>
	<body>
		<div id="page">
			<div id="screen">
				<h1>System Error</h1>
				<#if isTemplateError>
					<b>Template:</b> ${throwable.templateName} (${throwable.fileName})<br>
					<b>Exception:</b> ${throwable.cause.class.name}<br>
					<b>Message:</b> ${throwable.cause.message} <br>
					<b>Line:</b> ${throwable.lineNumber?string}, <b>Column:</b> ${throwable.columnNumber?string}<br>
					<b>Expression:</b> ${throwable.expression!}<br>
					<b>Source</b>:${throwable.fileName}<br>
					<div class="templateSource">${highlightError(templateTool.getRawText(throwable.templateName),throwable,3)}</div>
					<b>Stack Trace</b><div id="errorStackTrace" class="stackTrace">${stackTrace}</div>
				<#else>
					<p id="errorMessage">System Error: <b>${throwable}</b></p>
					<div id="errorStackTrace" class="stackTrace">${stackTrace}</div>	
				</#if>
			</div>
		</div>
	</body>
</html>
