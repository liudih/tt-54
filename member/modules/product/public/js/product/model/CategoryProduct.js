define(
		['jquery', 'jqueryjson'],
		function() {
			+(function($,window){
				function getQueryString(name) {
							    var url=location.href;
							    if (url.indexOf("?") == -1 || url.indexOf(name + '=') == -1) {
							        return '';
							    }
							    var queryString = url.substring(url.indexOf("?") + 1);
							    var arry= new Array();
							    var parameters = queryString.split("&");
							    var pos, paraName, paraValue;
							    for (var i = 0; i < parameters.length; i++) {
							        pos = parameters[i].indexOf('=');
							        if (pos == -1) {
							            continue;
							        }
							        paraName = parameters[i].substring(0, pos);
							        paraValue = parameters[i].substring(pos + 1);
							        if (paraName == name) {
							            var value= unescape(paraValue.replace(/\+/g, " "));
							            arry.push(value)
							        }
							    }
							     return arry.length>0?arry:null;
				}

				function clearParam(target,url,param){
					                   if(url.indexOf('?')!=-1){     
			                            	var reg= new RegExp(param,'g')
			                            	var u=url.replace(reg,'');
			                            	u=u.replace(/\?&+/g,'?').replace(/(&+)$/g,'')
			                            	$(target).attr('href',u);
							           	}else{

						                   $(target).attr('href',url);
							           	}
				}
				function buildParam(target){
					var key=target.data('key');
					var col=target.data('col');
					var param='attris='+col+":"+key;
					return param;
				}
				 var url=location.href;
				        var attrs=getQueryString('attris');
				        var $tags=$('#tags');
				        $.each(attrs,function(i,attr){
				        	var arryAttr=attr.split(':');
				        	if(arryAttr.length==2){
				        		var col=arryAttr[0];
					        	var key=arryAttr[1];
					            $('span[role=attr]').each(function(i){
					                 ($(this).data('col')==col && $(this).data('key')==key)?(function(than){
				                        $clonetag=$(than).clone(true).removeAttr('role');
				                         var param=buildParam($clonetag);
				                         clearParam($clonetag.find('a'),url,param)
				                         $tags.append($clonetag);
					                 	 $(than).addClass('spanActive')
					                 })(this):null;	
					            });
				        	}
				        	

				        });
				        $('span[role=attr] a').each(function(i){
					             var $parent=$(this).parent('span[role=attr]');
					             var param=buildParam($parent);
					             $parent.hasClass('spanActive')?(function(than){
				                 clearParam(than,url,param);
					             })(this):(function(than){
					             
				                        if(url.indexOf('?')!=-1){

						                   $(than).attr('href',url+'&'+param);
							           	}else{

						                   $(than).attr('href',url+'?'+param);
							           	}

					             })(this)
					           	
					     });

				})(jQuery,window);
		});

