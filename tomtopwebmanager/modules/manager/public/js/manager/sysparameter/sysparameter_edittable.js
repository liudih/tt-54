 
$(function(){
	var TableEditable = 'sysparameter_manager_table';
    window[TableEditable] = function() {
        return {
            //main function to initiate the module
            init: function () {
                function restoreRow(oTable, nRow) {
                    var aData = oTable.fnGetData(nRow);
                    var jqTds = $('>td', nRow);

                    for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                        oTable.fnUpdate(aData[i], nRow, i, false);
                    }

                    oTable.fnDraw();
                }
                
                
                function editRow(oTable, nRow) {
                	var aData = oTable.fnGetData(nRow);
                	
                	var websites = "";
                	 
                	$.ajax({ 
                        type : "get", 
                        url : js_SystemParameterRoutes.controllers.manager.SystemParameter.getAllWetsite().url, 
                        async : false, 
                        success : function(data){ 
                        	for(var i=0; i< data.length; i++){
                    			
                    			if(aData[1] == data[i].cname){
                    				websites = websites + "<option value="+data[i].iid+" selected='selected'>"+data[i].ccode+"</option>";
                    			}else{
                    				websites = websites + "<option value="+data[i].iid+">"+data[i].ccode+"</option>";
                    			}
                    			
                    		}
                        } 
                   }); 
                    
                    var jqTds = $('>td', nRow);
                    jqTds[0].innerHTML = '<input type="text" name="newiid" disabled class="m-wrap medium span6" value="' + aData[0] + '">';
                    jqTds[1].innerHTML = "<select name='iwebsiteid'>" + websites + "</select>";
                    jqTds[2].innerHTML = '<input type="text" name="cparameterkey" id="cparameterkey" class="m-wrap medium span6" value="' + aData[2] + '">';
                    jqTds[3].innerHTML = '<input type="text" name="cparametervalue" id="cparametervalue" class="m-wrap medium span6" value="' + aData[3] + '">';
                    jqTds[4].innerHTML = '<a class="edit" href="">Save</a>';
                    jqTds[5].innerHTML = '<a class="cancel" href="">Cancel</a>';
                }

                function saveRow(oTable, nRow, ilanguagetext, iwebsiteidtext) {
                    var jqInputs = $('input', nRow);
                    var jqselects = $('select', nRow);
                    oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                    oTable.fnUpdate(iwebsiteidtext, nRow, 1, false);
                    oTable.fnUpdate(jqInputs[1].value, nRow, 2, false);
                    oTable.fnUpdate(jqInputs[2].value, nRow, 3, false);
                    oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 4, false);
                    oTable.fnUpdate('<a class="delete" href="">Delete</a>', nRow, 5, false);
                    oTable.fnDraw();
                }

                function cancelEditRow(oTable, nRow) {
                    var jqInputs = $('input', nRow);
                    
                    var jqselects = $('select', nRow);
                    
                    oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                    
                    oTable.fnUpdate(jqselects[0].value, nRow, 1, false);
                    
                    oTable.fnUpdate(jqInputs[1].value, nRow, 2, false);
                    oTable.fnUpdate(jqInputs[2].value, nRow, 3, false);
                    oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 4, false);
                    oTable.fnDraw();
                }

                var oTable = $('#sysparameter_manager_table').dataTable({
                    "aLengthMenu": [
                        [5, 15, 20, -1],
                        [5, 15, 20, "All"] // change per page values here
                    ],
                    // set the initial value
                    "iDisplayLength": 5,
                    "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                    "sPaginationType": "bootstrap",
                    "oLanguage": {
                        "sLengthMenu": "_MENU_ records per page",
                        "oPaginate": {
                            "sPrevious": "Prev",
                            "sNext": "Next"
                        }
                    },
                    "aoColumnDefs": [{
                            'bSortable': false,
                            'aTargets': [0]
                        }
                    ]
                });
                
                
               
                
                jQuery('#sysparameter_manager_table_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
                jQuery('#sysparameter_manager_table_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
                jQuery('#sysparameter_manager_table_wrapper .dataTables_length select').select2({
                    showSearchInput : true //hide search box with special css class
                }); // initialzie select2 dropdown

                var nEditing = null;
                var nNew = false;

                $('#sysparameter_manager_table_new').click(function (e) {
                	e.preventDefault();

                    if (nNew && nEditing) {
                        if (confirm("Previose row not saved. Do you want to save it ?")) {
                            saveRow(oTable, nEditing, null,null); // save
                            $(nEditing).find("td:first").html("Untitled");
                            nEditing = null;
                            nNew = false;

                        } else {
                            oTable.fnDeleteRow(nEditing); // cancel
                            nEditing = null;
                            nNew = false;
                            
                            return;
                        }
                    }

                    var aiNew = oTable.fnAddData(['','','', '', '', '', '', '']);
                    var nRow = oTable.fnGetNodes(aiNew[0]);
                    editRow(oTable, nRow);
                    nEditing = nRow;
                    nNew = true;
                });

                $('#sysparameter_manager_table a.delete').live('click', function (e) {
                    e.preventDefault();

                    var nRow = $(this).parents('tr')[0];
                    var cname = nRow.title;
                    
                    if (confirm("你确定删除类型["+cname+"]吗?") == false) {
                        return;
                    }

                    var iid = nRow.id;
                    
                    $.ajax({
                        url: js_SystemParameterRoutes.controllers.manager.SystemParameter.deleteSysParameter(iid).url,
                        type: 'GET',
                        data: {
                        },
                        success: function(data) {
                        	if(data.errorCode==0){
                        		alert('删除成功');
                        		oTable.fnDeleteRow(nRow);
	           				}else if(data.errorCode==1){
	           					alert('删除失败');
	           				}
                        }
                    });
                });

                $('#sysparameter_manager_table a.cancel').live('click', function (e) {
                	e.preventDefault();

                    if (nNew) {
                        oTable.fnDeleteRow(nEditing);
                        nNew = false;
                    } else {
                        restoreRow(oTable, nEditing);
                        nEditing = null;
                    }
                });

                $('#sysparameter_manager_table a.edit').live('click', function (e) {
                    e.preventDefault();

                    /* Get the row as a parent of the link that was clicked on */
                    var nRow = $(this).parents('tr')[0];

                    if (nEditing !== null && nEditing != nRow) {
                        /* Currently editing - but not this row - restore the old before continuing to edit mode */
                        restoreRow(oTable, nEditing);
                        editRow(oTable, nRow);
                        nEditing = nRow;
                    } else if (nEditing == nRow && this.innerHTML == "Save") {
                        
                    	
                    	var id = $("#sysparameter_manager_table").find("input[name='newiid']").val();
                    	
                    	 var iwebsiteid = $("#sysparameter_manager_table").find("select[name='iwebsiteid']  option:selected").val();
                    	 var ilanguageid = 0;
                    	 
                    	 var ilanguagetext = $("#sysparameter_manager_table").find("select[name='ilanguageid']  option:selected").text();
                    	 var iwebsiteidtext = $("#sysparameter_manager_table").find("select[name='iwebsiteid']  option:selected").text();
                    	
                    	
                    	var cparameterkey = $("#sysparameter_manager_table").find("input[name='cparameterkey']").val();
                    	var cparametervalue = $("#sysparameter_manager_table").find("input[name='cparametervalue']").val();
                    	
                    	if(''==cparameterkey)
                    	{
                    		alert("KEY值不能为空");
                    		return;
                    	}
                    	if(''==cparametervalue)
                    	{
                    		alert("参数值不能为空");
                    		return;
                    	}
                    	
                    	
                    	//在保存还是更新前验证key是否存在
                    	$.ajax({
                            url: js_SystemParameterRoutes.controllers.manager.SystemParameter.validateKey(cparameterkey, iwebsiteid).url,
                            type: 'GET',
                            data: {
                            },
                            success: function(data) {
                            	if(data.errorCode==0){
                            		 
                                	if(typeof id == "undefined" || ''==id){
                                		$.ajax({
                                            url: js_SystemParameterRoutes.controllers.manager.SystemParameter.addSysParameter().url,
                                            type: 'POST',
                                            data: {
                                            	cparameterkey: cparameterkey,cparametervalue:cparametervalue,iwebsiteid:iwebsiteid,ilanguageid:ilanguageid
                                            },
                                            success: function(data) {
                                            	if(null !=data.iid && data.iid!=0  ){
                           						 
                                            		alert('添加成功');
                                            		
                                            		 /* Editing this row and want to save it */
                                            		$("#sysparameter_manager_table").find("input[name='newiid']").val(data.iid);
                                            		
                                            		nEditing.id = data.iid;
                                            		nEditing.title = data.cparameterkey;
                                            		
                                                    saveRow(oTable, nEditing, ilanguagetext,iwebsiteidtext);
                                                    nEditing = null;
                		           				}else{
                		           					alert('添加失败');
                		           				}
                                            }
                                        });
                                	}else
                            		{
                                		
                                		$.ajax({
                                            url:js_SystemParameterRoutes.controllers.manager.SystemParameter.alterSysParameter().url,
                                            type: 'POST',
                                            data: {
                                                iid: id,cparameterkey: cparameterkey,cparametervalue:cparametervalue,iwebsiteid:iwebsiteid,ilanguageid:ilanguageid
                                            },
                                            success: function(data) {
                                            	if(data.errorCode==0){
                                            		alert('更新成功');
                                            		
                                            		 /* Editing this row and want to save it */
                                            		
                                                    saveRow(oTable, nEditing, ilanguagetext,iwebsiteidtext);
                                                    nEditing = null;
                		           				}else if(data.errorCode==1){
                		           					alert('更新失败');
                		           				}
                                            }
                                        });
                                		
                            		}
                            		 
		           				}else if(data.errorCode==1){
		           					
		           					if(typeof id == "undefined" || ''==id){
		           						alert('您填写的KEY已经存在，请修改后再保存');
                                		
                                		return;
		           					}else{
		           						$.ajax({
                                            url: js_SystemParameterRoutes.controllers.manager.SystemParameter.alterSysParameter().url,
                                            type: 'POST',
                                            data: {
                                                iid: id,cparameterkey: cparameterkey,cparametervalue:cparametervalue,iwebsiteid:iwebsiteid,ilanguageid:ilanguageid
                                            },
                                            success: function(data) {
                                            	if(data.errorCode==0){
                                            		alert('更新成功');
                                            		
                                            		 /* Editing this row and want to save it */
                                                    saveRow(oTable, nEditing, ilanguagetext,iwebsiteidtext);
                                                    nEditing = null;
                		           				}else if(data.errorCode==1){
                		           					alert('更新失败');
                		           				}
                                            }
                                        });
	           						}
		           					
		           				}
                            }
                        });
                         
                    } else {
                        /* No edit in progress - let's start one */
                        editRow(oTable, nRow);
                        nEditing = nRow;
                    }
                });
            }

        };
    }();
    window[TableEditable].init();
});