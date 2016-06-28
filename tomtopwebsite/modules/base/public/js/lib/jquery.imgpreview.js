+(function($){
	
	function Preview(element,options){
		this.$element=$(element);
		this.$input=$(element).find(':file');
		this.$preview=$(options.preview);
		this.$form=$(element).find('form');
		
	}
	Preview.prototype={
			
			listen:function(){
				this.$input.on('change', $.proxy(this.change, this));
			},
			change:function(e, invoked){
				  var file = e.target.files !== undefined ? e.target.files[0] : (e.target.value ? { name: e.target.value.replace(/^.+\\/, '') } : null)
			      if (invoked === 'clear'){
			    	  return;
			      }
			      
				  if (!file) {
					  this.clear();
					  return;
			      }
				  if (typeof file.type !== "undefined" ? file.type.match('image.*') : file.name.match('\\.(gif|png|jpe?g)$') ) {  
					  
				  }else{
					  return;
				  }
				  this.preview();
				  
			},
			preview:function(){
				var url=this.$form.attr('action');
				this.$form.ajaxSubmit({
					type:'post',
					url:url,
					beforeSubmit: function(){
                        
                     },
                     success: function(data){                   
                       
                     },
                     resetForm: false,
                     clearForm: false
				});
			}
	}
	
	
	
	$.fn.preview=function(options){
		
		return $(this).each(function(){
			new Preview(this,options);
		});
	}
	
})(jQuery);