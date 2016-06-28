define(['jquery'],
		function($) {
			function ImagePreview() {
			}

			ImagePreview.prototype = {
					validateImg: function(data) {
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
				    },
				    
				    showPrvImg: function(src, prid) {
				        var img = document.createElement("img");
				        img.src = src;
				        var prvbox = $('#' + prid);
				        prvbox.append(img);
				    },
					
					validateImage: function(a, prid) {
						var $this = this;
						var file = a;
						var tip = "图片格式为:jpeg,gif,png!"; // 设定提示信息
					    var prvbox = $('#' +　prid);
					    prvbox.empty();
					    if (window.FileReader) { // html5方案
					        for (var i=0, f; f = file.files[i]; i++) {
					            var fr = new FileReader();
					            fr.onload = function(e) {
					                var src = e.target.result;
					                if (!$this.validateImg(src)) {
					                    alert(tip);
					                } else {
					                	$this.showPrvImg(src, prid);
					                }
					            }
					            fr.readAsDataURL(f);
					        }
					    } else { // 降级处理
					        if ( !/\.jpg$|\.png$|\.gif$/i.test(file.value) ) {
					            alert(tip);
					        } else {
					        	$this.showPrvImg(file.value, prid);
					        }
					    }
					}
					
			};
			
			return ImagePreview;

});

