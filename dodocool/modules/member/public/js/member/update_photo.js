$('#submit_id').click(function(){
	var form = $("#preview-form");
	var _url = form.attr("action");
	form.ajaxSubmit({  
        type:"post",  //提交方式  
        dataType:"json", //数据类型  
        url:_url, //请求url  
        error:function(err){
        	$("#show_error").html("Save failed!");
        },
        success:function(data){ //提交成功的回调函数  
        	if(data['errorCode']==2){
    			var message = data.errorMessage;
    			$("#show_error").html(message);
    		}else if(data['errorCode']==3){
    			var message = data.errorMessage;
    			$("#show_error").html(message);
    			
			}else if(data['errorCode']==1){
    			var message = data.errorMessage;
    			$("#show_error").html(message);
			} else {
				 var url = upload_picture.controllers.member.Edits.editsMember().url;
				 console.info(url);
				 window.location = url;
    		}
        }  
    });
	
	return false;
});

$('#filepath').change(function(){
	var id = $(this).attr('id');
	validateImage(this, 'prv' + id);
});

function validateImg(data) {
    var pos = data.indexOf(",") + 1;
    var filters = {
	        "jpeg"  : "/9j/4",
	        "gif"   : "R0lGOD",
	        "png"   : "iVBORw"
	    }
    for (var e in filters) {
        if (data.indexOf(filters[e]) === pos) {
            return e;
        }
    }
    return null;
};

function showPrvImg(src, prid) {
    $('#' + prid).attr('src', src);
};

function validateImage(a, prid) {
	var $this = this;
	var file = a;
	var tip = "Image format: jpeg,gif,png!"; // 设定提示信息
    var prvbox = $('#' +　prid);
    prvbox.empty();
    if (window.FileReader) { // html5方案
        for (var i=0, f; f = file.files[i]; i++) {
            var fr = new FileReader();
            fr.onload = function(e) {
                var src = e.target.result;
                if (!$this.validateImg(src)) {
                    $("#show_error").html(tip);
                } else {
                	$this.showPrvImg(src, prid);
                	$("#show_error").html("");
                }
            }
            fr.readAsDataURL(f);
        }
    } else { // 降级处理
        if ( !/\.jpg$|\.png$|\.gif$/i.test(file.value) ) {
            alert(tip);
        } else {
        	$("#show_error").html("");
        	$this.showPrvImg(file.value, prid);
        }
    }
}