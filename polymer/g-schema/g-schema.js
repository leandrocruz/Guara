define(function (require) {
	
	var $excel = require("gl!guara/excel");
	
	var _this = null;
	
	var _xmlSchemaParse = function($this, file, sheetNumber /* legacy */, whenDone, schema, sanitizer) 
	{
		try
		{
			$g.utils.read(file, 'text', function(e) {
				var data     = e.target.result;
				var parser   = new DOMParser();
				var doc      = parser.parseFromString(data, "application/xml");
				var tag      = schema.getTag();
				var elements = doc.querySelectorAll(tag);
				var array    = [];

				$.each(elements, function(idx, element) {
					var item = $this.toJson(schema, element);
					array.push(item);
				});						
				
				if(sanitizer)
				{
					$.each(array, function(idx, item){
						array[idx] = sanitizer(idx, item);
					});
				}

				$this.tableRender.render(file, schema, array);
				whenDone.success(file, sheetNumber, array);
			});
		}
		catch(e)
		{
			whenDone.error(file, sheetNumber, e);
		}
	};
	
	var _xlsSchemaParse = function($this, file, sheetNumber, whenDone, schema, sanitizer) 
	{
		try
		{
			$excel.sheetByIndex(file, sheetNumber, function(sheet) {

				//reading is async. Here we have the sheet.
				
				try
				{
					var array = $excel.toJson(sheet);
					if(sanitizer)
					{
						$.each(array, function(idx, item){
							array[idx] = sanitizer(idx, item);
						});
					}
					$g.log.info("Rendering sheet #" + sheetNumber + " with " + array.length + " rows");
					$this.tableRender.render(file, schema, array);
					whenDone.success(file, sheetNumber, array);
				}
				catch(e)
				{
					whenDone.error(file, sheetNumber, e);
				}
			});
		}
		catch(err)
		{
			whenDone.error(file, sheetNumber, err);
		}
	};
	
	var module = {

		parser: null,
		
		debug: false,
        
		created: function() {
			_this = this;
		},
		
		parse : function(file, sheetNumber, whenDone, schema, sanitizer) 
		{
			var name  = file.name;
			var f     = _xlsSchemaParse;
			var isXml = file.name.endsWith("xml");
			
			if(schema.constructor === Array && isXml)
			{
				schema = schema[1];
			}
			else
			{
				schema = schema[0];
			}						
			if(file.name.endsWith("xml"))
			{
				f = _xmlSchemaParse;
			}
			
			f(this, file, sheetNumber, whenDone, schema, sanitizer);
		},
		
		ready: function() {
        
        	this.tableRender = {
        		
        		table: this.$.table,

        		render: function(file, schema, array) {
        			if(console && console.table)
        			{
        				console.table(array);
        			}
        			this.table.clear();
        			this.table.title  = file.name;
        			this.table.schema = schema;
       				for(var i = 0; i < array.length; i++)
     				{
     					var row = array[i];
     					this.handleRow(row, i);
     				}
        		
                    this.data = array;
                    this.table.show();
        		},

				handleRow: function(row, i) {
					this.table.appendRow(row);
				}
        	};
        },
        
        toJson: function(schema, item) {
			var result = {};
        	$.each(schema, function(key, meta) {
				if(typeof meta !== 'function')
				{
					var value = null;
					if(meta.file)
					{
						result.files = [];
						var files = $(item).xpath(meta.xpath);
						$.each(files, function(idx, file){
							var name = file.querySelector(meta.file.name).textContent;
							var data = file.querySelector(meta.file.data).textContent;
							result.files.push({name: name, data: data});
							value = !value ? name : value + '|' + name; 
						});
					}
					else if(meta.xpath)
					{
						value = $(item).xpath(meta.xpath).text();
					}
					else
					{
						value = item.querySelector(key.toLowerCase()).textContent;
						
					}

					if(value 
							&& value.startsWith('![CDATA[')
							&& value.endsWith(']]'))
					{
						var start = '![CDATA['.length;
						value = value.substring(start, value.length - 2);
					}
					
					var k = meta.column ? meta.column.name : key;
					result[k] = value;
				}
			});
			return result;
        },

		clear: function() {
			this.$.table.clear();
		},
		
		unmark: function(rowNumber) {
			return this.$.table.unmark(rowNumber);
		},
		
		mark: function(rowNumber, columnName, issues) {
			return this.$.table.mark(rowNumber, columnName, issues);
		}
	};
	
	return module;
});
