 function loadTables(id) {
        var TableEditable = 'email_variable_manager_table';
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

                	var types = "";
                	$.post(email_variable.controllers.manager.EmailTemplate.getAllEmailTemplateTypes().url, function(data){
                		for(var i=0; i< data.length; i++){
                			types = types + "<option>"+data[i]+"</option>";
                		}
                	});
                  
                    
                    //文本框只能输入数字，不能输入小数点和字母
                    function onlyNum(id){
	                     $("#"+id).keydown(function(event){
	                    	 var keyCode = event.which; 
	                         if (keyCode == 46 || keyCode == 8 || keyCode == 37 || keyCode == 39 ||
	                            (keyCode >= 48 && keyCode <= 57) || (keyCode >= 96 && keyCode <= 105) ){
	                            	  return true; 
	                         }else{
	                              return false 
	                         }
	                      }).focus(function() {
	                    	  this.style.imeMode = 'disabled';
	                      }); 
                    }
                    
                    function editRow(oTable, nRow) {
                        var aData = oTable.fnGetData(nRow);
                        var jqTds = $('>td', nRow);
                        jqTds[0].innerHTML = '<input type="text" name="iid" disabled class="m-wrap small" value="' + aData[0] + '">';
//                      jqTds[1].innerHTML = '<input type="text" name="ctype"  class="m-wrap small" value="' + aData[1] + '">';
                        jqTds[1].innerHTML = "<select name='ctype'><option selected='selected'>"+aData[1]+"</option>" + types + "</select>";
                        jqTds[2].innerHTML = '<input type="text" name="cname" class="m-wrap small" value="' + aData[2] + '">';
                        jqTds[3].innerHTML = '<input type="text" name="cremark" class="m-wrap small" value="' + aData[3] + '">';
                        jqTds[4].innerHTML = '<input type="text" name="ccreateuser" disabled class="m-wrap small" value="' + aData[4] + '">';
                        jqTds[5].innerHTML = '<input type="text" name="dcreatedate" disabled class="m-wrap small" value="' + aData[5] + '">';
                        jqTds[6].innerHTML = '<a class="edit" href="">Save</a>';
                        jqTds[7].innerHTML = '<a class="cancel" href="">Cancel</a>';
                    }

                    function saveRow(oTable, nRow) {
                        var jqInputs = $('input', nRow);
                        var str = "";
                        if(jqInputs[1].value == ""){
                        	str = str + "模板类型不能为空\n";
                        }
                        if(jqInputs[2].value == ""){
                        	str = str + "模板名称不能为空\n";
                        }
                        if(jqInputs[1].value == ""){
                        	str = str + "模板备注不能为空\n";
                        }
                        if(str != "") {
                        	alert(str);
                        	return;
                        }
                        oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                        oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                        oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                        oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                        oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
                        oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);
                        oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 6, false);
                        oTable.fnUpdate('<a class="delete" href="">Delete</a>', nRow, 7, false);
                        oTable.fnDraw();
                    }

                    function cancelEditRow(oTable, nRow) {
                        var jqInputs = $('input', nRow);
                        oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                        oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                        oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                        oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                        oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
                        oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);
                        oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 6, false);
                        oTable.fnDraw();
                    }

                    var oTable = $('#email_variable_manager_table').dataTable({
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
                    
                    jQuery('#email_variable_manager_table_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
                    jQuery('#email_variable_manager_table_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
                    jQuery('email_variable_manager_table_wrapper .dataTables_length select').select2({
                        showSearchInput : false //hide search box with special css class
                    }); // initialzie select2 dropdown

                    var nEditing = null;
                    var nNew = false;

                    $('#email_variable_manager_table_new').click(function (e) {
                    	e.preventDefault();
                        if (nNew && nEditing) {
                            if (confirm("Previose row not saved. Do you want to save it ?")) {
                                saveRow(oTable, nEditing); // save
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
                        var aiNew = oTable.fnAddData(['', '', '', '', '', '', '', '']);
                        var nRow = oTable.fnGetNodes(aiNew[0]);
                        editRow(oTable, nRow);
                        nEditing = nRow;
                        nNew = true;
                    });
                    
                    $('#email_variable_manager_table a.delete').live('click', function (e) {
                        e.preventDefault();
                        var nRow = $(this).parents('tr')[0];
                        var cname = nRow.title;
                        if (confirm("你确定删除模板变量["+cname+"]吗?") == false) {
                            return;
                        }
                    	var data = JSON.stringify({"id":nRow.id});
                        $.ajax({
                            url: email_variable.controllers.manager.EmailTemplateVariable.deleteEmailVariable().url,
                            type: 'POST',
                            data: data,
                			contentType : "application/json",
                            success: function(data) {
                            	if(data.errorCode===0){
                            		oTable.fnDeleteRow(nRow);
		           				}else if(data.errorCode===1){
		           					alert('删除失败');
		           				}
                            }
                        });
                    });
                    
                    $('#email_variable_manager_table a.cancel').live('click', function (e) {
                    	e.preventDefault();
                        if (nNew) {
                            oTable.fnDeleteRow(nEditing);
                            nNew = false;
                        } else {
                            restoreRow(oTable, nEditing);
                            nEditing = null;
                        }
                    });

                    $('#email_variable_manager_table a.edit').live('click', function (e) {
                        e.preventDefault();
                        /* Get the row as a parent of the link that was clicked on */
                        var nRow = $(this).parents('tr')[0];
                        if (nEditing !== null && nEditing != nRow) {
                            /* Currently editing - but not this row - restore the old before continuing to edit mode */
                            restoreRow(oTable, nEditing);
                            editRow(oTable, nRow);
                            nEditing = nRow;
                        } else if (nEditing == nRow && this.innerHTML == "Save") {
                        	var id = $("#email_variable_manager_table").find("input[name='iid']").val();
                        	var ctype = $("#email_variable_manager_table").find("select[name='ctype']").find("option:selected").text();
                        	var cname = $("#email_variable_manager_table").find("input[name='cname']").val();
                        	var cremark = $("#email_variable_manager_table").find("input[name='cremark']").val();
                        	var str = "";
                        	if(cname == ""){
                        		str = str + "模板变量名称不能为空\n";
                        	}
                          	if(cremark == ""){
                          		str = str + "模板变量备注不能为空";
                        	}
                          	if(str != ""){
                          		alert(str);
                          		return false;
                          	}
                        	
                        	if(typeof id == "undefined" || ''==id){
                        		var data = JSON.stringify({"ctype": ctype,"cname":cname,"cremark":cremark});
                        		$.ajax({
                                    url: email_variable.controllers.manager.EmailTemplateVariable.addEmailVariable().url,
                                    type: 'POST',
                                    data: data,
                                	contentType : "application/json",
                                    success: function(data) {
                                    	if(null != data.iid && data.iid !=0  ){
                                    		alert('添加成功');
                                    		 /* Editing this row and want to save it */
                                    		$("#email_variable_manager_table").find("input[name='iid']").val(data.iid);
                                    		$("#email_variable_manager_table").find("input[name='ccreateuser']").val(data.user);
                                    		$("#email_variable_manager_table").find("input[name='dcreatedate']").val(data.date);
                                    		var ctypestr = "<input name='ctype' value='"+data.type+"'>";
                                    		$("#email_variable_manager_table").find("select[name='ctype']").replaceWith(ctypestr);
                                            saveRow(oTable, nEditing);
                                            nEditing = null;
        		           				}else{
        		           					alert('添加失败');
        		           				}
                                    }
                                });
                        	} else {
                        		var data = JSON.stringify({"iid":id,"ctype": ctype,"cname":cname,"cremark":cremark});
                        		$.ajax({
                                    url: email_variable.controllers.manager.EmailTemplateVariable.eiditEmailVariable().url,
                                    type: 'POST',
                                    data: data,
                                    contentType : "application/json",
                                    success: function(data) {
                                    	if(data.errorCode===0){
                                    		alert('更新成功');
                                    		 /* Editing this row and want to save it */
                                    		var ctypestr = "<input name='ctype' value='"+ctype+"'>";
                                    		$("#email_variable_manager_table").find("select[name='ctype']").replaceWith(ctypestr);
                                            saveRow(oTable, nEditing);
                                            nEditing = null;
        		           				}else if(data.errorCode===1){
        		           					alert('更新失败');
        		           				}
                                    }
                                });
                    		}
                        } else {
                            editRow(oTable, nRow);
                            nEditing = nRow;
                        }
                    });
                }

            };
        }();
        window[TableEditable].init();
    }