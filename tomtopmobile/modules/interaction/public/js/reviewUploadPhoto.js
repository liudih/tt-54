(function($){
	Array.prototype.remove=function(index){
		if(isNaN(index)||index>this.length){
			return false;
		}
		if(index>=this.length){
			return;
		}
		for(var i=0,n=0;i<this.length;i++) 
	    { 
	        if(this[i]!=index) 
	        { 
	            this[n++]=this[i] 
	        } 
	    } 
	    this.length-=1 
	}
	function PreviewList(element,options){
		this.$ele=$(element) ;
		this.$input=this.$ele.find(':file');
		this.$indexInput=this.$ele.find('input[name=index]');
		this.$previewForm=this.$ele.find('#writeReviewForm');
		this.$addCommentForm=this.$ele.find('#writeReviewForm');
		this.$addEle=this.$ele.find('.cursor');
		this.$msg=$("#imgmsg");
		this.currentNum=0;
		this.listener();
		this.guid=1;
		this.preViewIds="";
		this.enableupload=true;
		this.allowNum=5;
		this.allowSize=2;
	}
	PreviewList.prototype={
			listener:function(){
				var self = this;
				this.$input.change($.proxy(this.change, this));
				$(".deleteAddPic").click($.proxy(self,"remove"));
				//$(document).on('submit','#writeReviewForm',$.proxy( this.submit, this));
			},
			reset:function(){
		        this.$addCommentForm.find('input[name=indexs]').val('');
				this.clear();
				this.currentNum=0;
				this.$ele.find('li[id!=cursor]').remove();
				$('input[name=indexs]:eq(0)').val("");
				this.enableupload=true;
				this.$addEle.show();
				this.$msg.html('');
			},
			submitSuccess:function(data){
				if(data && data.errorCode===0){
					this.reset();
				}
			},
			submit:function(e){
				var url=this.$writeReviewForm.attr('action');
				return false;
			},
			clear:function(){
				var $cloneInput=this.$input.clone(true);
				$cloneInput.val('');
				this.$input.before($cloneInput);
				this.$input.remove();
				this.$input=$cloneInput;
			},
			muiltfiles:function(files){
				var arry = new Array();
				for(var i =0;i< files.length;i++){
					var file=files[i];
					if (!(typeof file.type !== "undefined" ? file.type.match('image.*') : file.name.match('\\.(gif|png|jpeg)$'))) {
						this.$msg.html(file.name+ " support JPEG GIF PNG JPG BMP!");
						if(files.length>1){
							continue;
						}else{
							this.clear();
							return;
						}
					}
	                var fileSize=file.size/(1023*1024);
	                if(fileSize>this.allowSize){
	                	this.$msg.html(file.name+" Image size 2M max!");
	                	if(files.length>1){
							continue;
						}else{
							this.clear();
							return;
						}
	                }
	                this.preview(this.guid);
	  			    var o={};
	  			    o.file=file;
	  			    o.guid=this.guid;
	  			    arry.push(o);
	  			    this.guid++;
	  			    ++this.currentNum;
	  				if(this.currentNum >=this.allowNum){
	  					this.$addEle.hide();
	  					this.upload(arry);
	  					this.enableupload=false;
	  					return ;
	  				} 
				}
				this.upload(arry);
			},
			ajaxEerror:function(){
			},
			ajaxSuccess:function(data){
				this.$msg.html("");
				if(data && data.errorCode===0){
					    var index=data.index;
						var imgurl = $("#getimgUrl").val()+"?index="+index
						+ "&_t=" + Math.random();
						var $preview=this.$ele.find('li[data-index="'+index+'"]');
						$preview.append('<img src="'+imgurl+'" />');
						$preview.find('div.throbber').remove();
						addPicH();	 //main.js调整高度
						this.clear();
						var ids = $('input[name=indexs]:eq(0)').val();
						ids += index+'|';
						$('input[name=indexs]:eq(0)').val(ids);
					}
			},
			ajaxUpload:function(formData){
				if(!this.enableupload){
					return;
				}
				var listingId = $("input[name='clistingid']").val();
				var _url = $("#addpicUrl").val();
				 var self=this;
				 $.ajax({
					 method:'post',
					 url:_url,
					 data:formData,
					 cache: false,
					 contentType: false,
					 processData: false,
					 success:function(data){
						 self.ajaxSuccess.call(self,data);
					 }
				 });
			},
			upload:function(arry){
				for(var i =0; i< arry.length; i++){
					var formData = new FormData();
					formData.append("index",arry[i].guid);
					formData.append("picture",arry[i].file);
					this.ajaxUpload(formData);
				}
			},
			change:function(e, invoked){
			    if(e.target.files){
			    	var files=e.target.files;
			    	this.muiltfiles(files);
			    	return false;
			    }
			},
			remove:function(e){
				var $target=$(e.target)
				var $preview=$target.parents('li');
				var index=$preview.data('index');
				var s= s || {};
				var self=this;
				s.url= $("#delimgUrl").val()+"?index="+index;
				s.data={};
				s.success=function(data){
					if(data){
						if(data.errorCode==0){
							$preview.remove();
							var dataIndex=data.index;
							var ids = $('input[name=indexs]:eq(0)').val();
							ids = ids.replace(dataIndex+"|","");
							$('input[name=indexs]:eq(0)').val(ids);
    						
							self.currentNum--;
							if(self.currentNum<self.allowNum){
								self.$addEle.show();
								self.enableupload=true;
							}
						}
					}
				}
				$.ajax(s);
			},
			preview:function(index){
				var node=this.$ele.find('li:first');
				var html='<li class="lineBlock" data-index='+index+'><div style="display:block;" class="throbber"></div><div class="deleteAddPic"></div></li>'
				node.before(html);
				var self = this;
				$(".deleteAddPic").click($.proxy(self,"remove"));
			}
	}
	$.fn.previewList=function(options){
		new PreviewList(this,options)
	}
})(jQuery);
$(function(){
	$('#postPhotos_popBox').previewList({});
});