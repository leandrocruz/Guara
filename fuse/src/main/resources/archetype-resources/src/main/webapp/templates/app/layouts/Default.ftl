<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="${content.getURI("/resources/css/footerStick.css")}" 	type="text/css" rel="stylesheet">
		<link href="${content.getURI("/resources/css/dojo.css")}" 			type="text/css" rel="stylesheet">
		<link href="${content.getURI("/resources/css/guara.css")}" 			type="text/css" rel="stylesheet">
		<link href="${content.getURI("/resources/css/app.css")}" 			type="text/css" rel="stylesheet">
		<#-- <script type="text/javascript" src="${content.getURI("/resources/js/dojo/dojo-0.4.2-ajax/dojo.js")}"></script> -->
		<script type="text/javascript" src="${content.getURI("/resources/js/guara.js")}"></script>
		<script type="text/javascript" src="${content.getURI("/resources/js/ajax.js")}"></script>
	</head>
	<body>
		<#--
		<script type="text/javascript">
			djConfig = {
				parseWidgets: true,
				isDebug: true,
				debugAtAllCosts: false,
				baseScriptUri: "${content.getURI("/resources/js/dojo/dojo-0.4.2-ajax/")}"
			};
			dojo.require("dojo.debug.console");
			dojo.require("dojo.widget.DropdownDatePicker");
			dojo.require("dojo.html.*");
			dojo.require("dojo.lfx.html");
			dojo.require("dojo.widget.ContentPane");
			dojo.require("dojo.widget.Tooltip");
		</script>
		-->
		<div id="page">
			<div id="nonFooter">
				<div id="progressIndicator">Processando</div>
				<div id="top">${templateTool.render("navigations.Top")}</div>
				<div id="frame">
					<div id="screen">${screen_placeholder}</div>
				</div>
			</div>
		</div>
		<div id="footer">
			<div id="bottom">${templateTool.render("navigations.Bottom")}</div>
		</div>
		<#--
		<@g.modal 	id="modalX" 
					style="width: 550px; height: 330px;"
					showHeader=false/>
		-->
	</body>
</html>
