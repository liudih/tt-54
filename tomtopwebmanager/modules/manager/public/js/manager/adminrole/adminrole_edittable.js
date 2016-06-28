 function loadEditTables() {
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
                        var jqTds = $('>td', nRow);
                        jqTds[0].innerHTML = '<input type="text" name="newiid" disabled class="m-wrap medium span6" value="' + aData[0] + '">';
                        jqTds[1].innerHTML = '<input type="text" name="crolename" id="crolename" class="m-wrap medium span6" value="' + aData[1] + '">';
                        jqTds[2].innerHTML = '<a class="edit" href="">Save</a>';
                        jqTds[3].innerHTML = '<a class="cancel" href="">Cancel</a>';
                    }

                    function saveRow(oTable, nRow) {
                        var jqInputs = $('input', nRow);
                        oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                        oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                        oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 2, false);
                        oTable.fnUpdate('<a class="delete" href="">Delete</a>', nRow, 3, false);
                        oTable.fnDraw();
                    }

                    function cancelEditRow(oTable, nRow) {
                        var jqInputs = $('input', nRow);
                        oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                        oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                        oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 2, false);
                        oTable.fnDraw();
                    }

                    var oTable = $('#adminrole_manager_table').dataTable({
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
                    
                    
                   
                    
                    jQuery('#adminrole_manager_table_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
                    jQuery('#adminrole_manager_table_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
                    jQuery('#adminrole_manager_table_wrapper .dataTables_length select').select2({
                        showSearchInput : true //hide search box with special css class
                    }); // initialzie select2 dropdown

                    var nEditing = null;
                    var nNew = false;

                    $('#adminrole_manager_table_new').click(function (e) {
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

                        var aiNew = oTable.fnAddData(['', '', '', '', '', '']);
                        var nRow = oTable.fnGetNodes(aiNew[0]);
                        editRow(oTable, nRow);
                        nEditing = nRow;
                        nNew = true;
                    });

                    $('#adminrole_manager_table a.delete').live('click', function (e) {
                        e.preventDefault();

                        var nRow = $(this).parents('tr')[0];
                        var cname = nRow.title;
                        
                        if (confirm("你确定删除类型["+cname+"]吗?") == false) {
                            return;
                        }

                        var iid = nRow.id;
                        
                        $.ajax({
                            url: js_AdminRoleRoutes.controllers.manager.AdminRole.deleteAdminRole(iid).url,
                            type: 'GET',
                            data: {
                            },
                            success: function(data) {
                            	alert(data.msg);
                            	if(data.errorCode===0){
                            		oTable.fnDeleteRow(nRow);
		           				}
                            }
                        });
                    });

                    $('#adminrole_manager_table a.cancel').live('click', function (e) {
                    	e.preventDefault();

                        if (nNew) {
                            oTable.fnDeleteRow(nEditing);
                            nNew = false;
                        } else {
                            restoreRow(oTable, nEditing);
                            nEditing = null;
                        }
                    });

                    $('#adminrole_manager_table a.edit').live('click', function (e) {
                        e.preventDefault();

                        /* Get the row as a parent of the link that was clicked on */
                        var nRow = $(this).parents('tr')[0];

                        if (nEditing !== null && nEditing != nRow) {
                            /* Currently editing - but not this row - restore the old before continuing to edit mode */
                            restoreRow(oTable, nEditing);
                            editRow(oTable, nRow);
                            nEditing = nRow;
                        } else if (nEditing == nRow && this.innerHTML == "Save") {
                            
                        	
                        	var id = $("#adminrole_manager_table").find("input[name='newiid']").val();
                        	var crolename = $("#adminrole_manager_table").find("input[name='crolename']").val();
                        	
                        	 
                        	if(''==crolename)
                        	{
                        		alert("角色名称不能为空");
                        		return;
                        	}
                   		 
                        	if(typeof id == "undefined" || ''==id){
                        		$.ajax({
                                    url: js_AdminRoleRoutes.controllers.manager.AdminRole.addAdminRole().url,
                                    type: 'POST',
                                    data: {
                                    	crolename: crolename
                                    },
                                    success: function(data) {
                                    	if(null !=data.iid && data.iid!=0  ){
                   						 
                                    		alert('添加成功');
                                    		
                                    		 /* Editing this row and want to save it */
                                    		$("#adminrole_manager_table").find("input[name='newiid']").val(data.iid);
                                    		nEditing.id = data.iid;
                                    		nEditing.title = data.crolename;
                                            saveRow(oTable, nEditing);
                                            nEditing = null;
        		           				}else{
        		           					alert('添加失败');
        		           				}
                                    }
                                });
                        	}else
                    		{
                        		
                        		$.ajax({
                                    url:js_AdminRoleRoutes.controllers.manager.AdminRole.updateAdminRole().url,
                                    type: 'POST',
                                    data: {
                                        iid: id,crolename: crolename
                                    },
                                    success: function(data) {
                                    	if(data.errorCode===0){
                                    		alert('更新成功');
                                    		
                                    		 /* Editing this row and want to save it */
                                            saveRow(oTable, nEditing);
                                            nEditing = null;
        		           				}else if(data.errorCode===1){
        		           					alert('更新失败');
        		           				}
                                    }
                                });
                        		
                    		}
                    		 
           				
                             
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
    }