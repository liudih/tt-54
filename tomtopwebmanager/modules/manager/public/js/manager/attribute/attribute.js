
define(['jquery','jvalidate','jmetadata','jqueryjson'],function($){
	
	$.metadata.setType("attr", "validate");
	
	$("button[name^='submitAttribute']").each(function(){
		$(this).click(function () {
			var languageName = $(this).attr("value");
			var languageid = $(this).attr("languageid");
			var keymap = {};
			var keyvaluemap = {};
			var returnFlag = false;
			$("input[name^='" + languageName + "']").each(function(){
				var $this = $(this);
				var id = $this.attr("id");
				var valuelist = [];
				var i = 0;
				$("input[languagename^='" + languageName + "']").each(function(){
					var $value = $(this);
					var valuemap = {};
					var ilanguageid = $value.attr("ilanguageid");
				    var ivalueid = $value.attr("ivalueid");
				    var	cvaluename = $value.val();
				    if (cvaluename == "") {
				    	
				    	returnFlag = true;
				    }
					var id = $value.attr("id");
					valuemap["id"] = id;
					valuemap["ivalueid"] = ivalueid;
					valuemap["ilanguageid"] = ilanguageid;
					valuemap["cvaluename"] = cvaluename;
					valuelist[i++] = valuemap;
				});
				keyvaluemap["id"] = id;
				keyvaluemap["ilanguageid"] = languageid;
				keyvaluemap["ikeyid"] = $this.attr("ikeyid");
				var ckeyname = $this.val();
				if (ckeyname == "") {
					
					returnFlag = true;
			    }
				keyvaluemap["ckeyname"] = ckeyname;
				keyvaluemap["valuemap"] = valuelist;
			});
			if (returnFlag) {
				alert("存在为空字段!");
				return false;
			}
			console.info(keyvaluemap);
//			return false;
			data = $.toJSON(keyvaluemap);
			$.ajax({
				url : "/sysadmin/attribute/update",
				type : "post",
				data : data,
				contentType: "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
					if (true == data) {
						var url = attributeRoutes.controllers.manager.Attribute.getAttributeList(languageid).url;
						window.location.href = url;
					} else {
						alert(data);
					}
				}
			});
		});
	});
	
//	$("button[name^='addSubmitAttribute']").each(function(){
//		$(this).click(function () {
////			$(".horizontal-form").validate();
//			var languageName = $(this).attr("value");
//			var keymap = {};
//			var languageid = $(this).attr("languageid");
//			keymap["languageid"] = languageid;
//			$("input[name^='" + languageName + "']").each(function(){
//				var $this = $(this);
//				var id = $this.attr("id");
//				keymap["keyid"] = id;
//				keymap["keyname"] = $this.val();
//				var languageidmap = {};
//				var keyvaluemap = {};
//				var valuemap = {};
//				$("input[ilanguagename^='" + languageName + "']").each(function(){
//					var $value = $(this);
//					var ivalueid = $value.attr("ivalueid");
//					valuemap[ivalueid] = $value.val();
//				});
//				keymap["value"] = valuemap;
//			});
//			console.info(keymap);
////			return false;
//			data = $.toJSON(keymap);
//			$.ajax({
//				url : "/sysadmin/attribute/save",
//				type : "post",
//				data : data,
//				contentType: "application/json; charset=utf-8",
//				dataType : "json",
//				success : function(data) {
//					if (true == data) {
//						var url = attributeRoutes.controllers.manager.Attribute.getAttributeList(languageid).url;
//						window.location.href = url;
//					} else {
//						alert(data);
//					}
//				}
//			});
//		});
//	});
	
	$('#languageSelect').change(function(){
		var languageid = $("#languageSelect").val();
		if ("-1" == languageid) {
    		return false;
    	}
		var url = attributeRoutes.controllers.manager.Attribute.getAttributeList(languageid).url;
    	window.location.href = url;
	});
	
	
	$('#add-attribute-value').on('click', function(){
		var $div = $('#add-attribute-text');
		var $text = "<div><div class='controls'  style='margin-bottom:10px;'>" +
				"<input type='text' value='' name='cvaluename' placeholder='属性值' class='m-wrap medium span6'></div>" +
				"<div class='controls' style='position:absolute;left:229px;margin-top:-45px;'>" +
				"<span name='subAttribute' class='btn blue' id='sub-attribute-value'>-</span></div></div>";
		$div.append($text);
		subAttribute()
	});
	
	function subAttribute() {
		$("span[name^='subAttribute']").each(function(){
			$(this).unbind('click');
			$(this).on('click',function(){
				$(this).parent().parent().remove();
			});
			
		});
	}
	
	$('#add-attribute').on('click', function(){
		var map = {};
		var ckeyvalue = $("input[name^='ckeyname']").val();
		map['keyname'] = ckeyvalue;
		var valuenamelist = new Array();
		$("input[name^='cvaluename']").each(function(){
			var $this = $(this);
			var cvaluename = $(this).val();
			console.info(cvaluename);
			valuenamelist.push(cvaluename);
		});
		map['valuename'] = valuenamelist;
		
		console.info(map);
		var url = attributeRoutes.controllers.manager.Attribute.addAttribute().url;
		$.ajax({
			url : url,
			type : "post",
			data : $.toJSON(map),
			contentType: "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				if (true == data) {
					alert("success");
					location.href = attributeRoutes.controllers.manager.Attribute.getAttributeList().url;
				}
				
			}
		});
	});
	
	$('#add-attribute-value').on('click', function(){
		var form = $('#addAttributeValue');
		var url = form.attr("action");
		var $this = $('#add-attribute-value');
		var alanguageid = $this.attr("alanguageid");
		var akeyid = $this.attr("akeyid");
		$.post(url, form.serialize(), function(html) {
			location.href = "/sysadmin/attribute/manager?categoryid="+ akeyid + "&languageid=" + alanguageid;
		}, "html");
	});
});


$(function(){
	$("#add-attribute-modal").on("hidden", function() {
	    $(this).removeData("bs.modal");
	});
});

