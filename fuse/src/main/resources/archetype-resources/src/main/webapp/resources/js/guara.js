var Guara = {
	version: '0.0.1',
	currentModal:null
}

dojo.require("dojo.widget.Dialog");
dojo.require("dojo.widget.ContentPane");
dojo.require("dojo.html.common");


Guara.loadModalWindowSize = function(modalId, url, width, height)
{
	/* resize parent div. Needed on IE, FF works fine */
	modal = dojo.byId(modalId);
	modal.style.width = width+"px";
	modal.style.height = height+"px";
	
	contentId = modalId+":content";
	contentWindow = dojo.byId(contentId);
	contentWindow.style.width = width+"px";
	contentWindow.style.height = height+"px";

	Guara.loadModalWindow(modalId,url);
}

Guara.loadModalWindow = function(modalId, url)
{
	dojo.debug("loading dialog: "+modalId+" url:"+url);
	contentId = modalId+":content";
	dojo.byId(contentId).innerHTML = "";
	if(url) 
	{
		Ajax.loadContent(url,contentId);	
	}
	currentModal = dojo.widget.byId(modalId);
	currentModal.show();
}

Guara.closeModalWindow = function(modalId)
{
	currentModal = dojo.widget.byId(modalId);
	currentModal.hide();
	currentModal = null;
}

var totalTraversed = 0;

Guara.traverse = function(root, handler)
{
	root = dojo.byId(root);
	totalTraversed++;
	/*
	debug = dojo.byId("traverseDebug");
	if(false && debug)
	{
		txt = document.createTextNode('Handling node: '+root.tagName+' ('+root.id+') - '+root.childNodes.length);
		br = document.createElement('br');
		debug.appendChild(txt);
		debug.appendChild(br);
	}
	*/
	handler(root);
	
	for (var i=0; i<root.childNodes.length; i++) 
	{
		child = root.childNodes[i];
		Guara.traverse(child, handler);
	}
}

Guara.enableEditInPlace = function(rootId)
{
	//alert(totalTraversed);
	root = dojo.byId(rootId);
	if(!root)
	{
		alert("Root not found: "+rootId);
	}
	Guara.traverse(root, function(node) 
	{
		if(node.nodeType != dojo.dom.TEXT_NODE)
		{
			tagName = node.tagName;
			/*
			 * Swap class names
			 */
			if(tagName == "TR"  || tagName == "INPUT"
				&& (dojo.html.hasClass(node,"guaraEdit") || dojo.html.hasClass(node,"guaraShow")))
			{
				if(dojo.html.hasClass(node,"guaraEdit"))
				{
					dojo.html.replaceClass(node, "guaraShow", "guaraEdit");
				}
				else if(dojo.html.hasClass(node,"guaraShow"))
				{
					dojo.html.replaceClass(node, "guaraEdit", "guaraShow");
				}
			}
			
			/*
			 * Check if we need to "reset" the form values
			 */
			if(tagName == "TD" && node.id)
			{
				tmp = node.id.split(":");
				fieldName = tmp[0];
				descriptor = tmp[1];
				if("static" == descriptor)
				{
					field = dojo.byId(fieldName);
					status = dojo.html.getAttribute(field,"guaraStatus");
					//dojo.debug('field: '+field.id+' status: '+status);
					if(status == "error" || status == "dirty")
					{
						Guara._reset(field, node);
						Guara.removeAllMarkers(field.id);
					}
				}
			}
		}
	});
	dojo.debug("traversed "+totalTraversed+" nodes from: "+rootId);
}

Guara._reset = function(field, node)
{
	value = node.innerHTML;
	trimmed = dojo.string.trim(value);
	type = dojo.html.getAttribute(field,"type");
	dojo.debug("Reset field: "+field.id+" ("+field.tagName+":"+type+") from: ["+field.value+"]");
	switch(field.tagName)
	{
		case "INPUT":
			switch(type)
			{
				case "file":
					field.value = "";
					break;			
				default:
					field.value = trimmed;
			}
			break;
		case "SELECT":
			options = field.options;
			count = options.length;
			for(i = 0; i < count ; i++)
			{
				if(options[i].text == trimmed)
				{
					field.selectedIndex = i;
					break;
				}
			}
			break;
		default:
			field.value = trimmed;
	}
	dojo.debug("Reset field: "+field.id+" ("+field.tagName+":"+type+") to: ["+field.value+"]");
}
	
Guara.updateStaticValue = function(name, value)
{
	field = dojo.byId(name);
	//status = dojo.html.getAttribute(field,"guaraStatus");
	//dojo.debug(name+":"+status);
	if(field /* && status == "dirty" */)
	{
		dojo.debug("Updating static value of field "+name+" to: "+value)
		field.innerHTML = value;
	}
}

Guara.addErrorMarker = function(name, errorMessage)
{
	dojo.debug("Adding error marker to: "+name);
	field = dojo.byId(name);
	if(field)
	{
		if(errorMessage)
		{
			/*
			 * Error ToolTip
			 */
			overlay = dojo.byId(name+':errorTooltip:overlay');
			if(overlay)
			{
				txt = document.createTextNode(errorMessage);
				if(overlay.firstChild)
				{
					dojo.dom.replaceNode(overlay.firstChild,txt);
				}
				else
				{
					overlay.appendChild(txt);
				}
			}
		}
		dojo.html.addClass(field, "formError");
		field.setAttribute("guaraStatus","error");
		td = dojo.byId(name+':label');
		dojo.html.addClass(td, "formError");
	}
}

Guara.removeErrorMarker = function(name)
{
	//dojo.debug("Removing error marker from: "+name);
	field = dojo.byId(name);
	if(field)
	{
		/*
		 * Error ToolTip
		 */
		overlay = dojo.byId(name+':errorTooltip:overlay');
		if(overlay && overlay.firstChild)
		{
			overlay.removeChild(overlay.firstChild);
		}
		dojo.html.removeClass(field, "formError");
		td = dojo.byId(name+':label');
		dojo.html.removeClass(td, "formError");
	}
}

Guara.addDirtyMarker = function(evt,name)
{
	field = dojo.byId(name);
	if(field)
	{
		dojo.debug("Adding dirty marker to: "+name+", value: ["+field.value+"]");
		dojo.html.addClass(field, "dirty");
		field.setAttribute("guaraStatus","dirty");
	}
}

Guara.removeAllMarkers = function(name)
{
	field = dojo.byId(name);
	if(field)
	{
		status = dojo.html.getAttribute(field,"guaraStatus");
		if(status == "error" || status == "dirty")
		{
			dojo.debug("Removing markers from: "+name);
			dojo.html.removeClass(field, "formError");
			dojo.html.removeClass(field, "dirty");
			td = dojo.byId(name+':label');
			dojo.html.removeClass(td, "formError");
			field.setAttribute("guaraStatus","");
		}
	}
}

Guara.setAction = function(form, actionName)
{
	var form = dojo.byId(form);
	var nodeId = form.id+":actionName";
	var node = dojo.byId(nodeId);
	//alert(node != null ? "action: "+node.name : " no action defined");
	if(node != null)
	{
		node.name = "exec_"+actionName;
	}
	else
	{
		node = document.createElement("input");
		node.id = nodeId;
		node.type = "hidden";
		node.name = "exec_"+actionName
		form.appendChild(node);
	}
	node.value = 1;
}

Guara.submitForm = function(formId)
{
	form = dojo.byId(formId);
	Ajax.eval(form);
}

Guara.getValue = function(node)
{
	node = dojo.byId(node);
	switch(node.tagName)
	{
		case "INPUT":
			return node.value;
			break;
		case "SELECT":
			selected = node[node.selectedIndex];
			return selected.value;
			break;
		default:
			return '';
	}
}

Guara.setValue = function(node, value)
{
	node = dojo.byId(node);
	switch(node.tagName)
	{
		case "INPUT":
			node.value = value;
			break;
		case "SELECT":
			var options = node.options;
			var count = options.length;
			for(i = 0; i < count ; i++)
			{
				if(options[i].value == value)
				{
					node.selectedIndex = i;
					break;
				}
			}
			break;
		default:
	}
}

react = function(evt)
{
	var target = evt.originalTarget.id;
	dojo.debug("event target: "+target+" event: "+evt.type);
	var root = target;
	if(target.indexOf(':') > 0)
	{
		var index = target.indexOf(':');
		root = target.substring(0,index);
	}
	var eventMap = dojo.byId(root+":events");
	if(eventMap)
	{
		var children = eventMap.childNodes;
		var count = children.length;
		for (var i=0; i<count; i++) 
		{
			var tmp = children[i];
			if(dojo.dom.ELEMENT_NODE == tmp.nodeType)
			{
				var eventType = tmp.getAttribute("type");
				//dojo.debug("Event: "+tmp.id +':'+eventType);
				if(eventType == evt.type || eventType == 'on'+evt.type)
				{
					var action = tmp.getAttribute("action");
					var runAt  = tmp.getAttribute("runAt");
					if('server' == runAt)
					{
						var firstChar = '?';
						if(action.indexOf('?') > 0)
						{
							firstChar = '&';
						}
						var value = Guara.getValue(target);
						action = action + firstChar+'target='+target+'&value='+value+'&event='+evt.type;
						Ajax.eval(action);	
					}
					else if('client' == runAt)
					{
						dojo.debug('Executing function: '+action);
						eval(action+'(evt)');
					}
				}
			}
		}
	}
}
