define(['jquery'],
		function($) {
			function Recommendlabel() {
			}

			Recommendlabel.prototype = {
					initTable: function() {
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
						});
						$('#oderlist a.delete').on('click', function (e) {
							$this.deleteRecommendLabel(this);
					    });
					},
					
					submit : function() {
						var form = $("#searchRecommendBase");
						var url = form.attr("action");
						var $this = this;
						$.post(url, form.serialize(), function(html) {
							$("#search_recommendlabelbase_html").replaceWith(html);
							$this.initTable();
						}, "html");
					},
					
					changePageSize : function(e) {
						$("a[tag=pageSize]").attr("href", "javascript:;");
						$("a[tag=pageSize]").removeAttr("class");
						$(e).attr("class", "showAc");
						$(e).removeAttr("href");
						var status = $(e).attr("value");
						$("input[name=pageSize]").val(status);
					},
					
					changePageNum : function(e) {
						$("a[tag=pageNum]").attr("href", "javascript:;");
						$("a[tag=pageNum]").removeAttr("class");
						$(e).attr("class", "showAc");
						$(e).removeAttr("href");
						var status = $(e).attr("value");
						$("input[name=pageNum]").val(status);
					},
					
					
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
				    
				    showPrvImg: function(src) {
				        var img = document.createElement("img");
				        img.src = src;
				        var prvbox = $('#prvid');
					    prvbox.innerHTML = "";
				        prvbox.append(img);
				    },
					
					validateImage: function(a) {
						var $this = this;
						var file = a;
						var tip = "图片格式为:jpeg,gif,png!"; // 设定提示信息
					    var prvbox = $('#prvid');
					    prvbox.innerHTML = "";
					    if (window.FileReader) { // html5方案
					        for (var i=0, f; f = file.files[i]; i++) {
					            var fr = new FileReader();
					            fr.onload = function(e) {
					                var src = e.target.result;
					                if (!$this.validateImg(src)) {
					                    alert(tip);
					                } else {
					                	$this.showPrvImg(src);
					                }
					            }
					            fr.readAsDataURL(f);
					        }
					    } else { // 降级处理
					        if ( !/\.jpg$|\.png$|\.gif$/i.test(file.value) ) {
					            alert(tip);
					        } else {
					        	$this.showPrvImg(file.value);
					        }
					    }
					}
					
			};
			
			return Recommendlabel;

});

