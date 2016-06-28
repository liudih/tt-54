+(function($){
	     function Country(element,options){
    	     this.$ele=$(element);
             this.$itemlist= this.$ele.find('.countrylist_ul');
             this.$input=this.$ele.find('h3');           
             this.change=options.change;
             this.$search=this.$ele.find('input[name=search]');
             this.listener();
	     }
	     Country.prototype={
                 listener:function(){
                   // this.$ele.find('.select_country').on('click',$.proxy(this.togger,this));
                    $(document).on('click',$.proxy(this.togger,this));
                    this.$search.on('keyup',$.proxy(this.search,this));
                 },
                 
                 togger:function(e){
                	 var e=e || window.event;
                	 var target=e.target;
                	 if($(target).data('target')==='togger'){
                		 this.$ele.find('.country_all').show();
                		 e.preventDefault();
                		 return;
                		 
                	 }
                	 if($(target).data('target')==='select'){
                		 this.select(e);
                		 e.preventDefault();
                		 return;
                	 }
                	 this.$ele.find('.country_all').hide();
                	 e.preventDefault();
                 },
                 
                 setVal:function(code,text){
                	this.$input.removeClass();
      				this.$input.addClass("flag_" + code);
      				this.$input.children("span").text(text);
      				this.$input.children("input").val(text);
      				this.$input.children("input").attr({countryName:text});
                 },
                 
                 
                 select:function(e){
                	var $currentLiNode = $(e.currentTarget);
                	var code = $currentLiNode.data('code');
                	var countrylist_ul_Tag = $currentLiNode.parent("#countrylist_ul").attr("tag");
                	var countryValue = $currentLiNode.find("span[name=country]").attr("value");
                	
                    var $target=$(e.target);
     				var text = $target.text();
     				this.setVal(code,text);
     				if(typeof this.change==='function'){
     					this.change.call(this,countryValue,countrylist_ul_Tag);
     				}
     				
                 },
                 
                 search:function(e){
                	this.$search.focus();
		            var key = this.$search.val().toLowerCase();
		        	var items = this.$itemlist.find('li');
		            if(this.$search.val()===""){
		            	$.each(items, function(i, item) {
		            		$(item).show();
		            	});
		            	return;
		            }
					var str = '.*(' + key + ').*';
					var reg = new RegExp(str, 'i');
				
					$.each(items, function(i, item) {
						var txt = $(item).find('span').text().toLowerCase();
							if (reg.test(txt)) {
								$(item).show();
							} else {
								$(item).hide();
							}
					});
				}
			}
			$.fn.country = function(options) {
				return $(this).each(function() {
					new Country(this, options);
				});
			}
		})(jQuery);
		
		