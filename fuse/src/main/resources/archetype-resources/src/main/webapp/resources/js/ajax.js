var Ajax = {
	version: '0.0.1',
	pi:null
}

/**
 * Calls exec and displays the data received from server using containerId
 */
Ajax.loadContent = function(obj,containerId) {
	Ajax.exec(obj, function(data) {
		var container = dojo.byId(containerId);
		dojo.debug('container: '+ containerId);
		container = dojo.widget.manager.getWidgetById(containerId);
		container.setContent(data);
	});
}

/**
 * Calls exec and evaluates the data received from server
 */
Ajax.eval = function(obj) {
	Ajax.exec(obj, function(data) {
		eval(data);
	});
}

Ajax._showLoadingMessage = function() {
	Ajax.pi = dojo.byId("progressIndicator");
	Ajax.pi.style.display = "block";
	dojo.html.setOpacity(Ajax.pi,1);
	document.body.style.cursor = 'wait';	
}

Ajax._hideLoadingMessage = function() {
	dojo.lfx.html.fadeHide(Ajax.pi,500,null,null).play();
	document.body.style.cursor = 'default';
}

Ajax.exec = function(obj,handler) {
	/* display loading message */
	Ajax._showLoadingMessage();
	
	/* submit request */ 
	var param = {
		useCache: false,
		preventCache: true,
		handle: function(type, data, evt) 
		{
			Ajax._hideLoadingMessage();
			if(type == "load")
			{
				handler(data);
			} 
			else if(type == "error")
			{
				dojo.debug("Error: "+data.message)
				alert("Error sending request: "+data.message);
			}
		}
	}

	if(dojo.lang.isString(obj)) 
	{
		dojo.debug("submiting request to: "+obj);
		param.url = obj;
	} 
	else if(obj.tagName == "FORM") 
	{
		dojo.debug("submiting request to: "+obj.action);
		param.formNode = obj;
	}
	dojo.io.bind(param);
}