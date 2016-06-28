
/**
 * columns : [ { 'index' : '', 'name' : '', 'width' : 100, 'align' : 'center',
 * 'css' : '', 'hidden' : , 'render' : function(data,record,datas){
 *  } } ]
 * @author lijun
 */
(function ($) {
	//添加scroll event
	$(window).scroll(function(){
		var st = $(this).scrollTop();
		var dh = $(document).height();
		var wh = window.screen.availHeight;
		if (Math.abs(dh - st - wh) < 200) {
			for(var g in $.fn.bsgrid.gridObjs){
				var grid = $.fn.bsgrid.gridObjs[g];
				grid.nextPage();
			}
			
		}
	});
    $.fn.bsgrid = {
        // defaults settings
        defaults: {
        	tableClass : 'accTable span98',
            url: '',
            otherParames: {},
            autoLoad: true,
            pageSize: 30,
            requestParamsName: {
                pageSize: 'pageSize',
                curPage: 'curPage',
            },
            columns : []
        },

        gridObjs: {},

        init: function (gridId, settings) {
        	
            var options = {
                settings: $.extend(true, {}, $.fn.bsgrid.defaults, settings),
                gridId: gridId,
                otherParames: settings.otherParames,
                totalRows: 0,
                totalPages: 0,
                curPage: 1,
                curPageRowsNum: 0,
                startRow: 0,
                endRow: 0
            };

            $('#' + gridId).addClass(options.settings.tableClass);
            
            if ($('#' + gridId).find('thead').length == 0) {
                $('#' + gridId).prepend('<thead></thead>');
                $('#' + gridId + ' thead').prepend('<tr></tr>');
                var td = [];
                $(options.settings.columns).each(function(i,c){
                	td.push('<th ');
                	if(c.css){
                		td.push(' class="');
                		td.push(c.css);
                		td.push('"');
                	}
                	if(c.width){
                		td.push(' width=');
                		td.push(c.width);
                	}
                	if(c.align){
                		td.push(' style="');
                		td.push('text-align:');
                		td.push(c.align);
                		td.push('"');
                	}
                	td.push('>');
                	
                	if(c.name){
                		td.push(c.name);
                	}
                	
                	td.push('</th>');
                });
                $('#' + gridId + ' thead tr').prepend(td.join(''));
            }
            if ($('#' + gridId).find('tbody').length == 0) {
                $('#' + gridId + ' thead').after('<tbody></tbody>');
            }
            
            var gridObj = {
                options: options,
                getPageCondition: function (curPage) {
                    return $.fn.bsgrid.getPageCondition(curPage, options);
                },
                page: function (curPage) {
                    $.fn.bsgrid.page(curPage, options);
                },
                search: function (params) {
                    $.fn.bsgrid.search(params, options);
                },
                loadGridData: function (dataType, gridData) {
                    $.fn.bsgrid.loadGridData(dataType, gridData, options);
                },
                createPagingOutTab: function () {
                    $.fn.bsgrid.createPagingOutTab(options);
                },
                clearGridBodyData: function () {
                    $.fn.bsgrid.clearGridBodyData(options);
                },
                getPagingObj: function () {
                    return $.fn.bsgrid.getPagingObj(options);
                },
                getCurPage: function () {
                    return $.fn.bsgrid.getCurPage(options);
                },
                refreshPage: function () {
                    $.fn.bsgrid.refreshPage(options);
                },
                nextPage: function () {
                	if(!this.options.loading && this.options.totalPages && this.pagingObj.curPage < this.options.totalPages){
                		this.options.loading = true;
                		this.pagingObj.curPage = this.pagingObj.curPage + 1;
                     	this.page(this.pagingObj.curPage);
                	}
                },
                initPaging: function () {
                    return $.fn.bsgrid.initPaging(options);
                },
                setPagingValues: function () {
                    $.fn.bsgrid.setPagingValues(options);
                }
            };

            // store mapping grid id to gridObj
            $.fn.bsgrid.gridObjs[gridId] = gridObj;


            gridObj.pagingObj = gridObj.initPaging();
            try {
                var minWidth = $.trim($('#' + options.pagingId).children().width());
                minWidth = minWidth == '' ? 0 : parseInt(minWidth);
                if (minWidth != 0) {
                    $('#' + gridId).css('min-width', minWidth + 16);
                    $('#' + options.pagingOutTabId).css('min-width', minWidth + 16);
                }
                $('#' + options.pagingOutTabId).width($('#' + gridId).width());
                $(window).resize(function () {
                    $('#' + options.pagingOutTabId).width($('#' + gridId).width());
                });
            } catch (e) {
            }

            if (options.settings.isProcessLockScreen) {
                $.fn.bsgrid.addLockScreen(options);
            }

            if (options.settings.autoLoad) {
                setTimeout(function () {
                    gridObj.page(1);
                }, 10);
            }

            return gridObj;
        },

        getGridObj: function (gridId) {
            var obj = $.fn.bsgrid.gridObjs[gridId];
            return obj ? obj : null;
        },

        parseJsonData: {
            success: function (json) {
                return json.success;
            },
            totalRows: function (json) {
                return json.totalRows;
            },
            curPage: function (json) {
                return json.curPage;
            },
            data: function (json) {
                return json.data;
            },
            getRecord: function (data, row) {
                return data[row];
            },
            getColumnValue: function (record, index) {
                return $.trim(record[index]);
            },
            getDataLen : function(json){
            	 return json.data.length;
            }
        },


        getPageCondition: function (curPage, options) {
            var params = [];
            for (var key in options.otherParames) {
                params.push(key + '=' + options.otherParames[key]);
            }
            params.push(options.settings.requestParamsName.pageSize + '=' + options.settings.pageSize);
            params.push(options.settings.requestParamsName.curPage + '=' + curPage);
            params = params.join('&');
            return params;
        },

        search: function (params, options) {
            options.otherParames = params;
            $.fn.bsgrid.page(1, options);
        },

        page: function (curPage, options) {
            if ($.trim(curPage) == '' || isNaN(curPage)) {
                return;
            }
            var grid = $.fn.bsgrid.getGridObj(options.gridId);
            var para = $.extend({},grid.pagingObj,options.otherParames);
           
            var dataType = 'json';
            $.ajax({
                type: 'post',
                url: options.settings.url,
                data: para,
                dataType: 'json',
                beforeSend: function (XMLHttpRequest) {
                    
                },
                complete: function (XMLHttpRequest, textStatus) {
                    
                },
                success: function (gridData, textStatus) {
                	options.loading = false;
                    $.fn.bsgrid.loadGridData(dataType, gridData, options);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                	
                }
            });
        },

        loadGridData: function (dataType, gridData, options) {
            var parseSuccess = $.fn.bsgrid.parseJsonData.success(gridData);
            if (parseSuccess) {

                var totalRows = parseInt($.fn.bsgrid.parseJsonData.totalRows( gridData));
                var curPage = parseInt($.fn.bsgrid.parseJsonData.curPage( gridData));
                curPage = Math.max(curPage, 1);


                var pageSize = options.settings.pageSize;
                var totalPages = parseInt(totalRows / pageSize);
                totalPages = parseInt((totalRows % pageSize == 0) ? totalPages : totalPages + 1);
                var curPageRowsNum = $.fn.bsgrid.parseJsonData.getDataLen(gridData);
                curPageRowsNum = curPageRowsNum > pageSize ? pageSize : curPageRowsNum;
                curPageRowsNum = (curPage * pageSize < totalRows) ? curPageRowsNum : (totalRows - (curPage - 1) * pageSize);
                var startRow = (curPage - 1) * pageSize + 1;
                var endRow = startRow + curPageRowsNum - 1;
                startRow = curPageRowsNum <= 0 ? 0 : startRow;
                endRow = curPageRowsNum <= 0 ? 0 : endRow;

                options.totalRows = totalRows;
                options.totalPages = totalPages;
                options.curPage = curPage;
                options.curPageRowsNum = curPageRowsNum;
                options.startRow = startRow;
                options.endRow = endRow;

                
                if (curPageRowsNum == 0) {
                    return;
                }

                var data = $.fn.bsgrid.parseJsonData.data( gridData);
                var dataLen = data.length;
                
                var ele = [];
                if($.isArray(options.settings.columns)){
                	$(data).each(function(index,data){
                		
                		ele.push('<tr>');
                		$(options.settings.columns).each(function(i,d){
                			ele.push('<td ');
                			if(d.align){
                				ele.push('style="text-align:');
                				ele.push(d.align);
                				ele.push('"');
                			}
                			ele.push('>');
                			if($.isFunction(d.render)){
                				ele.push(d.render(data));
                			}else{
                				if(d.index){
                					ele.push(data[d.index]);
                				}
                			}
                			ele.push('</td>');
                		});
                		ele.push('</tr>');
                	});
                }
                return $('#' + options.gridId + ' tbody').append(ele.join(''));
            } 
        },

        getPagingObj: function (options) {
            return $.fn.bsgrid.getGridObj(options.gridId).pagingObj;
        },

        getCurPage: function (options) {
            return $.fn.bsgrid.getPagingObj(options).curPage;
        },

        refreshPage: function (options) {
            if (!options.settings.pageAll) {
                $.fn.bsgrid.getPagingObj(options).refreshPage();
            } else {
                $.fn.bsgrid.page(1, options);
            }
        },

        initPaging: function (options) {
            return {
                'pageSize' : options.settings.pageSize,
                'curPage' : 1,
            }
        },

        setPagingValues: function (options) {
            $.fn.bsgrid.getPagingObj(options).curPage = options.curPage;
            $.fn.bsgrid.getPagingObj(options).totalRows = options.totalRows;
        }

    };

})(jQuery);