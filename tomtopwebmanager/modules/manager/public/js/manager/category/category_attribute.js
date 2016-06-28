    $('#update-category-attribute').click(function() {
    	var attributeKeyId = $('#attributeKeyId').val();
    	updateCategoryAttribute(attributeKeyId);
    });
    
    $('#add-category-attribute').click(function() {
    	var attributeKeyid = $("#attributeSelect").val();
    	updateCategoryAttribute(attributeKeyid);
    });
    
    function updateCategoryAttribute(attributeKeyId) {
//    	var attributeKeyId = $('#attributeKeyId').val();
    	var categoryid = $('#categoryid').val();
    	var languageid = $('#languageid').val();
    	var valueid = "";
    	$("input[type='checkbox']:checked").each(function(){
    	     var iattributevalueid = $(this).val();
    	     valueid = valueid + iattributevalueid + ";"
    	}); 
    	var map = {};
    	map["categoryid"] = categoryid;
    	map["attributeKeyId"] = attributeKeyId;
    	map["valueid"] = valueid;
    	var url = categoryRoutes.controllers.manager.Category.updateCategoryAttribute().url;
    	$.ajax({
			url : url,
			type : "post",
			data : JSON.stringify(map),
			contentType: "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				if (true == data) {
					alert("success");
					location.href = categoryRoutes.controllers.manager.Category.getCategoryAttributeManager(categoryid, languageid).url;
				}
				
			}
		});
    }
    
    
    
    function attributeSelectChange() {
    	$('#attributeSelect').unbind("change");
    	$('#attributeSelect').change(function(){
    		var attributeKeyid = $("#attributeSelect").val();
    		if ("-1" == attributeKeyid) {
    			$("#addAttribute").empty();
	    		
	    		return false;
	    	}
	    	var categoryid = $('#categoryid').val();
	    	var languageid = $('#languageid').val();
	    	var url = categoryRoutes.controllers.manager.Category.getAttributeToCategory(categoryid, attributeKeyid, languageid).url;
	    	$.get(url, function(data){
				$("#addAttribute").replaceWith(data);
			}, "html");
		});
    }
    
    $("#sample_editable_1_new").click(function(){
    	setInterval(function(){
    		attributeSelectChange();
    	},500); 
    });
    
    $('#sample_1 a.delete').on('click', function (e) {
        e.preventDefault();
        if (confirm("Are you sure to delete this row ?") == false) {
            return;
        } 
        var attributeKeyId = $(this).data('id');
        updateCategoryAttribute(attributeKeyId);
    });

	$("#edit-category-attribute-modal").on("hidden", function() {
	    $(this).removeData("modal");
	});
	
	$("#add-category-attribute-modal").on("hidden", function() {
	    $(this).removeData("modal");
	});

