define(['jquery'],
		function($) {
			function Categorylabel() {
			}

			Categorylabel.prototype = {
					initTable: function() {
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
						});
						$('#oderlist a.delete').on('click', function (e) {
							$this.deleteCategoryLabel(this);
					    });
					},
					
					submit : function() {
						var form = $("#searchCategoryBase");
						var url = form.attr("action");
						var $this = this;
						$.post(url, form.serialize(), function(html) {
							$("#search_categorylabebase_html").replaceWith(html);
							$("#categoryLabelForm").on("submit", function(){
								$this.addCategoryLabel(this);
								return false;
							});
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
					
					addCategoryLabel: function(){
						var categoryids = [];
						var categorynames = [];
						var i = 0;
						$("#categeroytree :checked").each(function(){
							if($(this).hasClass("chooseDefault")==false) {
								var id = $(this).attr('value');
								var categoryname = $(this).attr('catgoryname');
								categorynames[i] = categoryname;
								categoryids[i++] = id;
							}
						});
						var type = $('#type').val();
						if (categoryids.length <= 0) {
							alert("当前没有新增品类!");
							return false;
						} else {
							var alertMsg = "是否为品类" + categorynames +"增加"+ type +"标签?"
							if (confirm(alertMsg) == false)
								return false;
						}
						var data = {};
						var websiteId = $('#siteId').val();
						data['type'] = type;
						data['websiteId'] = websiteId;
						data['categoryids'] = categoryids;
						console.info(data);
						var url = categorylabelRoutes.controllers.manager.CategoryLabel.saveCategoryLabel().url;
						var $this = this;
						$.ajax({
							url : url,
							type : "post",
							data : $.toJSON(data),
							contentType: "application/json; charset=utf-8",
							dataType : "json",
							success : function(data) {
								console.info(data);
								if (data['result'] == true) {
									alert("添加成功!");
									$("#add-categorylabel-modal").hide();
									$this.submit();
								} else {
									alert("添加失败!");
								}
							}
						});
						
					},
					
					changeSelect: function() {
						var type = $("#type").val();
						var siteId = $("#siteId").val();
						var $this = this;
						if ("" == siteId || "" == type) {
							$('#search_categorylabebase_html').empty();
							return false;
						}
						$this.submit();
					},
					
					deleteCategoryLabel: function(e)　{
				        if (confirm("是否删除该品类标签?删除后该品类标签的数据将被清空!") == false) {
				            return;
				        } 
				        var labelid = $(e).data('id');
				        var url = categorylabelRoutes.controllers.manager.CategoryLabel.deleteCategoryLabel(labelid).url;
				        var $this = this;
				        $.ajax({
							url : url,
							type : "post",
							success : function(data) {
								if (data['result'] == true) {
									alert("删除成功!");
									$this.submit();
								} else {
									alert("删除失败，请重新操作!");
								}
							}
						});
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
			
			return Categorylabel;

});

