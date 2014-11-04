define(function (require) {
	
	var $excel = require("gl!guara/excel");
	
	var _this = null;
	
	var module = {

		parser: null,
		
		debug: false,
        
		created: function() {
			_this = this;
		},
		
        ready: function() {
        
        	this.tableRender = {
        		
        		table: this.$.table,

        		render: function(file, schema, array) {
       				
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

		clear: function() {
			this.$.table.clear();
		},
		
		parse: function(file, sheetNumber, whenDone, schema, sanitizer) {
			var $this = this;
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
