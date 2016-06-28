$(function(){
	$("#add_activity_application_rules_btn").click(function(){
		
		var activity_application_str =
			"<tr>" +
				"<td>" +
					"<select class='ruleselect' name=\"activity_rule_attr\">" +
					"<option value =\"1\">SKU</option>" +
					"<option value =\"2\">Color</option>" +
					"<option value =\"3\">Category</option>" +
					"</select> " +
				"</td>" + 
				
				"<td>" +
					"<select  name=\"activity_rule_operator\">" +
						"<option value =\"1\" selected=\"selected\">大于</option>" +
						"<option value =\"2\">等于</option>" +
						"<option value =\"3\">小于</option>" +
						"<option value =\"4\">包含</option>" +
						"<option value =\"5\">不包含</option>" +
					"</select>" +
				"</td>" +
				
				"<td>" +
					"<input type=\"text\"  name=\"activity_rule_value_key\" placeholder=\"KEY值，如：SKU\" class=\"m-wrap medium span6\">" +
				"</td>" +  
				
				"<td>" +
					"<a class=\"cancel\" ><i class=\"icon-trash\"></i></a>" +
				"</td>" + 
			"</tr> "; 
		$("#activity_application_rules_table").append(activity_application_str); 
	});
	
	/*删除选中行*/
	$(".cancel").live("click",function(){
		$(this).parents("tr").remove();  
	});  
	
	
	$(document).on("change","#activity_rule_select",function(){
		  var iruletype = $("#activity_rule_select  option:selected").val();
		  if(iruletype==1 || iruletype ==2){
			  $("#activity_preferential_value").show();
			  $("#activity_rules_value").hide();
		  }else{
			  $("#activity_preferential_value").show();
			  $("#activity_rules_value").show();
			  
		  }
		 
	});
	
	
	
	 $(document).on("click","#save_activity_application_rules_btn",function(){
		  //规则选择项
		 var activity_rule_select = $("#activity_rule_select  option:selected").val();
		 
		 var activity_rules_value = $("input[name='activity_rules_value']").val();
		 var activity_preferential_value = $("input[name='activity_preferential_value']").val();
		
		 var activity_rule_selectText = $("#activity_rule_select").find("option:selected").text();
		 
		//需要添加判断，如果一条规则对应两条属性，并且key值需要有值的情况，则需要添加两条记录
		var trlength = $("#activity_application_rules_table").find("tr").length;
		for(var i = 0 ; i <trlength; i ++)
		{
			var ikey = $("input[name='activity_rule_value_key']")[i].value;
			if(typeof ikey == "undefined" || "" ===ikey)
			{
				$.ajax({
					   type: "POST",
					   async:false,
					   url: js_ActivityRoutes.controllers.manager.Activity.addRuleRelation().url,
					   error: function(){},
					   success: function(msg){
					      //返回数据，动态添加行
						   var table= $("#activity_rule_detail_table");
						   var vTr= "<tr>" +
						   		"<td>"+activity_rule_selectText+"</td>" +
						   		"<td>"+activity_rules_value+"</td>" +
						   		"<td>"+activity_preferential_value+"</td>" +
						   		"<td></td>" +
						   		"<td></td>" +
						   		"<td></td>" +
						   		"<td><a href=\"javascript:deleteActivity_relation(3);\"><i class=\"icon-trash\"></i></a></td>"
						   		"</tr>"
						   table.append(vTr);
					   }
				});
				continue;
			}else
			{
				//发送请求，保存数据
				$.ajax({
				   type: "POST",
				   async:false,
				   url: js_ActivityRoutes.controllers.manager.Activity.addRuleRelation().url,
				   error: function(){},
				   success: function(msg){
					   
					   var activity_rule_attrText = $("select[name='activity_rule_attr']").find("option:selected")[i].text;
					   var activity_rule_operatorText = $("select[name='activity_rule_operator']").find("option:selected")[i].text;
					   var activity_rule_value_keyText = $("input[name='activity_rule_value_key']")[i].value;
				      //返回数据，动态添加行
					   var table= $("#activity_rule_detail_table");
					   var vTr= "<tr>" +
					   		"<td>"+activity_rule_selectText+"</td>" +
					   		"<td>"+activity_rules_value+"</td>" +
					   		"<td>"+activity_preferential_value+"</td>" +
					   		"<td>"+activity_rule_attrText+"</td>" +
					   		"<td>"+activity_rule_operatorText+"</td>" +
					   		"<td>"+activity_rule_value_keyText+"</td>" +
					   		"<td><a href=\"javascript:deleteActivity_relation(3);\"><i class=\"icon-trash\"></i></a></td>"
					   		"</tr>"
					   table.append(vTr);
				   }
				});
			}
		}
		
		  
	 });
	 
	 
	 /**
	  * 删除一行活动规则关联数据
	  */
	 function deleteActivity_relation(id)
	 {
		 if (confirm("Are you sure to delete this row ?") == false) {
			 return;
		 }
		 $.ajax({
			 type: "POST",
			 url: js_ActivityRoutes.controllers.manager.Activity.deleteRuleRelation().url,
			 error: function(){},
			 success: function(data){
				 if(data.errorCode===0){
					 $('#activity_rule_detail_table #'+data.iid+'').remove();
					 
				 }else if(data.errorCode===1){
					 
				 }
			 }
		 });
		 
	 }
  
});