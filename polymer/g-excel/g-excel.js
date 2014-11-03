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

        		render: function(file, array) {
       				
        			this.table.clear();
        			this.table.title = file.name;
        			this.handleHeader(array[0]);
       				for(var i = 0; i < array.length; i++)
     				{
     					var row = array[i];
     					this.handleRow(row, i);
     				}
        		
                    this.data = array;
                    this.table.show();
    			},
    			
				handleHeader: function(header) {
					var columns = [];
						
					$.each(header, function(key, value) {
						var column = {name: key, title: key};
						if(_this.debug)
						{
							console.log('Header: ', column);
						}
						columns.push(column);
					});
						
					this.table.columns = columns;
				},

				handleRow: function(row, i) {
					this.table.appendRow(row);
				}
        	}
        },

		clear: function() {
			this.$.table.clear();
		},
		
		parse: function(file, sheetNumber, whenDone, sanitizer) {
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
								array[idx] = sanitizer(item);
							});
						}
						$g.log.info("Rendering sheet #" + sheetNumber + " with " + array.length + " rows");
						$this.tableRender.render(file, array);
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
		
		mark: function(rowNumber, columnName, marker) {
			this.$.table.mark(rowNumber, columnName, marker);
		}
	};
	
	return module;
});
