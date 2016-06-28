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
		this.$msg=this.$ele.find('.msg');
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
				this.$input.on('change', $.proxy(this.change, this));
				$(document).on('click','div.addDele',$.proxy(this.remove, this));
				$(document).on('submit','#writeReviewForm',$.proxy( this.submit, this));
			},
			reset:function(){
		        this.$addCommentForm.find('input[name=indexs]').val('');
				this.clear();
				this.currentNum=0;
				this.$ele.find('li[id!=cursor]').remove();
				this.preViewIds = "";
				$('input[name=indexs]:eq(0)').val(this.preViewIds);
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
				this.$writeReviewForm.find('input[name=indexs]').val(this.preViewIds.join('|'));
				var self=this;
				this.$writeReviewForm.ajaxSubmit({
					type : 'post',
					url : url,
					beforeSubmit : function() {
					},
					error : function() {
					},
					success :function(data){
						self.submitSuccess.call(self,data);
					}
				});
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
  			    arry.push(o)
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
				if(data && data.errorCode===0){
					    var index=data.index;
						var imgurl = "/interaction/product-photos/get-preveiw?index="+index
						+ "&_t=" + Math.random();
						var $preview=this.$ele.find('li[data-index="'+index+'"]');
						$preview.append('<img src="'+imgurl+'" />');
						$preview.find('div.throbber').remove();
						this.clear();
						this.preViewIds += index+'|';
						$('input[name=indexs]:eq(0)').val(this.preViewIds);
					}
			},
			ajaxUpload:function(formData){
				if(!this.enableupload){
					return;
				}
				var listingId = $("input[name='clistingid']").val();
				var _url = js_reviewRoutes.controllers.interaction.review.ProductReview
				.doAddPicture(listingId).url
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
				s.url = "/interaction/product-photos/del-preveiw?index="+index;
				s.data={};
				s.success=function(data){
					if(data){
						if(data.errorCode==0){
							$preview.remove();
							var dataIndex=data.index;
							self.preViewIds = self.preViewIds.replace(dataIndex+"|","");
							$('input[name=indexs]:eq(0)').val(self.preViewIds);
    						
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
				var html='<li data-index='+index+'><div style="display:block;" class="throbber"></div><div class="addDele"></div></li>'
				node.before(html);
			}
	}
	$.fn.previewList=function(options){
		new PreviewList(this,options)
	}
})(jQuery);
$(function(){
	$('#postPhotos_popBox').previewList({});
});