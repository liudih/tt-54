+function(){
	 var QueryParam=function(){
		  this.storage=[];
	  };
	  QueryParam.prototype.add=function(name,value){
		  if(typeof name!='string'){
			  return;
		  }
		  if(value=='' || value==undefined){
			  this.remove(name);
			  return ;
		  }
		  for(var i in this.storage){
			  if(value==this.storage[name]){
				  return;
			  }
			  
		  }
		  this.storage[name]=value;
	  }
	  QueryParam.prototype.get=function(name){
		  return this.storage[value];
	  }
	  QueryParam.prototype.remove=function(name){
		  if(typeof name!='string'){
			  return;
		  }
		  delete this.storage[name];
	  }
	  
	var queryParam = new QueryParam;
	function initParam(){
		var $form=$('#search-form');
		var aid=$form.find('input[name=aid]').val();
		var salerid=$form.find('select[name=salerid]').val();
		var skuornum=$form.find('input[name=skuornum]').val();
		var begindate=$form.find('input[name=begindate]').val();
		var enddate=$form.find('input[name=enddate]').val();
		var orderstatus=$form.find('select[name=status]').val();
		var website=$form.find('select[name=website]').val();
		queryParam.add('aid',aid);
		queryParam.add('salerid',salerid);
		queryParam.add('skuornum',skuornum);
		queryParam.add('begindate',begindate);
		queryParam.add('enddate',enddate);
		queryParam.add('status',orderstatus);
		queryParam.add('website',website);
	}
	initParam();
	var table = $('#report_table').dataTable({
	      "aLengthMenu": [
	          [20],
	          [20]
	      ],
	      "iDisplayLength": 20,
	      "bFilter": false, 
	      "sPaginationType": "bootstrap",
	      "oLanguage": {
	    	   "sProcessing": "loading......",
	           "sLengthMenu": "page size _MENU_ ",
	           "sZeroRecords": "no found data!！",
	           "sEmptyTable": "no found data!！",
	           "sInfo": "from _START_ to _END_ record. total _TOTAL_ record",
	           "sInfoFiltered": "total _MAX_ record",
	           "sSearch": "search",
	           "sInfoEmpty" : "no found data!",
	           "sZeroRecords" : "no found data!",
	          "oPaginate": {
	        	  "sFirst": "first",
	              "sPrevious": "previou",
	              "sNext": "next",
	              "sLast": "last"
	          }
	      },
	      "aoColumns":[ 
		               
		               	{ "mData": "date", 'sClass':'center',"bSortable" : false},
		               	{ "mData": "paymentDate", 'sClass':'center',"bSortable" : false},
		            	{ "mData": "aid", 'sClass':'center',"bSortable" : false},
		            	{ "mData": "saler", 'sClass':'center',"bSortable" : false},
		               	{ "mData": "orderNum", 'sClass':'center',"bSortable" : false},
		             	{ "mData": "amount", 'sClass':'center',"bSortable" : false,
		            		"mRender" : function(data, type, row) {
		            			return '$'+data;
		            		}},
		             	{ "mData": "commission", 'sClass':'center',"bSortable" : false,
			            		"mRender" : function(data, type, row) {
			            			return '$'+data;
			            		}},
	            		{ "mData": "postage", 'sClass':'center',"bSortable" : false,
	            			"mRender" : function(data, type, row) {
	            				return '$'+data;
	            			}},
		             	{ "mData": "sku", 'sClass':'center',"bSortable" : false},
		             	{ "mData": "source", 'sClass':'center',"bSortable" : false},
		             	{ "mData": "statusName", 'sClass':'center',"bSortable" : false},
		             	{ "mData": "website", 'sClass':'center',"bSortable" : false}
	               ],
	      "bProcessing":true,
	      "bServerSide":true, 
	      "sAjaxSource":js_orderReportRoutes.controllers.manager.OrderReport.list().url, 
	      "fnServerParams": function ( aoData ) {
	    	  var start,totail,page,pageSize;
	    	  for(var i in aoData){
	    		  if(aoData[i].name=='iDisplayStart'){
	    			  start=aoData[i].value;
	    		  }
	    		  if(aoData[i].name=='iDisplayLength'){
	    			  totail=aoData[i].value;
	    			  pageSize=aoData[i].value;
	    		  }
	    	  }
	    	  page=(start==1)?start:parseInt(start/totail+1);
	          aoData.push( { "name": "page", "value": page } );
	          aoData.push( { "name": "pageSize", "value": pageSize } );
	          for(var name in queryParam.storage){
	        	  var value=queryParam.storage[name]
	        	  aoData.push( { "name":name, "value": value } );
	          }
	      },
	      "fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
	    	 
	          oSettings.jqXHR = $.ajax( {
	            "dataType": 'json',
	            "type": "POST",
	            "url": sSource,
	            "data": aoData,
	            "success": function(data,status,xhr){
	            	total=data.iTotalRecords
	            	showTotal(data);
	            	fnCallback(data,status,xhr);
	             },
	             "error":function(msg){
	            	
	             }
	          } );
	        }
	      
	  });
	

	function showTotal(data){
		var saleTotal=data.saleTotal;
    	var commissionTotal=data.commissionTotal;
    	$('#sale-total').html(saleTotal);
    	$('#commission-total').html(commissionTotal);
	}
	
	function search(){
		  var oSettings = table.fnSettings();
	      table.fnClearTable(0);
	      table.fnDraw();
	}
	
	$('#search-form').on('submit',function(){
		initParam();
		search();
		return false;
	});
}()
$(document).ready(function() {
	
 $('.date').datetimepicker({
　　format : 'dd/mm/yyyy',
	weekStart : 1,
	todayBtn : 1,
	autoclose : 1,
	todayHighlight : 1,
	startView : 2,
	minView : 2,
	forceParse : 0
  });
});