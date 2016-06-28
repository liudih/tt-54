	heartbeat();
	datafeed();
	
	
		var DispClose = true;
		window.onbeforeunload = function()
		{
			if(DispClose){
				return "";
			}
		}
		$(window).unload(function()
		{
			if(DispClose){
				logout();
			}
			DispClose = true;
		});
		window.onkeydown = function (){
			var ev = window.event || arguments[0];
			var code = ev.keyCode || ev.which;
			if (code==116) { 
		         if(ev.preventDefault) {
		            ev.preventDefault();
		        }else {
		            ev.keyCode=0;
		            ev.returnValue=false; 
		        }
		        refresh($("#myself_otherAlias").val()); 
			}
		}
		window.oncontextmenu = function(){
			return false;
		}
		
		function redirect(url){
		    DispClose = false;
			location.href = url;
		}
	

	function quit() {
		var ctl = {
			type : "QUIT",
		};
		sendControl(ctl);
	}
	
	function logout() {
		var ctl = {
			type : "LOGOUT",
		};
		sendControl(ctl);
	}
	
	function sendControl(control, dataCallback, errorCallback, doneCallback) {
		$.ajax({
			method : "POST",
			url : "/livechat/control",
			data : JSON.stringify(control),
			dataType : "json",
			contentType : "application/json",
			processData : false,
			success : dataCallback,
			error : errorCallback,
		}).done(doneCallback);
	}
	function sendData(sessionid, text, dataCallback, errorCallback, doneCallback) {
		var msg = text != null ? {
			text : text,
		} : {};
		$.ajax({
			method : "POST",
			url : "/livechat/data/"+sessionid,
			data : JSON.stringify(msg),
			dataType : "json",
			contentType : "application/json",
			processData : false,
			success : dataCallback,
			error : errorCallback
		}).done(doneCallback);
	}
	function accept(originAlias,topicstr) {
		var ctl = {
			'type' : "ACCEPT",
			'alias' : originAlias,
			'topic' : topicstr
		};
		sendControl(ctl);
	}
	
	function refresh(originAlias) {
		var ctl = {
			'type' : "REFRESH",
			'alias' : originAlias
		};
		
		$.ajax({
		    url: '/livechat/refresh',
		    dataType:'json', 
		    contentType: "application/json", 
		    data:JSON.stringify(ctl),
		    type: 'POST',
		    success: function(data) {
		    	 
		    }
		});
	}
	
	function hangup(id) {
		var ctl = {
				'type' : "HANGUP",
				'id' : id
		};
		sendControl(ctl);
	}
	
	function heartbeat() {
		var myselfAlias=$("input[name='myself_otherAlias']").val();
		var pingctl = {
			'type' : "PING",
			'alias' : myselfAlias
		};
		sendControl(pingctl, function(data) {
			$.each(data, function(i, d) {
				switch (d.type) {
				case "RING":
					accept(d.originAlias,d.topic);
					break;
				case "ESTABLISHED":
					var otherAlias = d.otherAlias;
					refresh(otherAlias);
					break;
				case "REFRESH":
					var sessionid = d.id;
					var otherAlias = d.alias;
					
					 $("#sessionidlist ul li").each(function(index,li){
						  if(li.id == sessionid){
							  
						  }else{
							newchatroom(sessionid, otherAlias, d.ip, d.country)
						  }
					 });
					break;
				case "TRANSFER" :
					var roleId=$("input[name='livechat_roleId']").val();
					var connet ={
						type : "CONNECT",
						destination : d.to,
						ilanguageid : 1,
						iroleid : roleId
					}
					sendControl(connet,function(d1){
						var number = 1;
						if(d1.status=='WAIT'){
							number = d1.number;
						}
						redirect("/livechat/waiting/" + d.topic+"/"+number);
					});
					break;
				case "CLOSED":
					// 添加信息告诉对方
					var sessionid = d.id;
					$('#'+sessionid+'_message').append("The other side has closed chat!");
					break;
				}
			});
		}, function() {
			
		}, function() {
			setTimeout(heartbeat, 2000);
		});
	}
	
	
	function datafeed() {
		// 此处需要 获取 sessionidlist的值，循环从后台获取信息
		var sessionidlist = $("#sessionidlist ul li");

		$.each(sessionidlist, function(i, li) {
			sendDataImpl(li.id, null);

		});
		setTimeout(datafeed, 2000);
		
	}
	
	function sendDataImpl(sessionid, text){
		var currentsessionid =  $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		var myselfAlias=$("input[name='myself_otherAlias']").val();
		sendData(sessionid,text, function(data) {
			$.each(data, function(i, msg) {
	    		if(msg.senderAlias == myselfAlias){
	    			
	    			$('#'+sessionid+'_message').append(
				 	 '<div class="liveChat_showR">'+
                         '<div class="lineBlock liveChat_text">'+
                             '<div class="liveChat_nameDate">'+
                                 '<div class="lineBlock liveChat_name">'+msg.senderAlias+'</div>'+
                                 '<div class="lineBlock liveChat_date">'+msg.sendDate+'</div>'+
                             '</div>'+
                             '<div class="liveChat_infm">'+msg.text+'</div>'+
                         '</div>'+
                         '<span class="lineBlock liveDefault_head"></span>'+
                     '</div>');
	    			
	    		}else{
	    			$('#'+sessionid+'_message').append(
						 '<div class="liveChat_showL">'+
						 '<span class="lineBlock liveDefault_head"></span>'+
                         '<div class="lineBlock liveChat_text">'+
                             '<div class="liveChat_nameDate">'+
                                 '<div class="lineBlock liveChat_name">'+msg.senderAlias+'</div>'+
                                 '<div class="lineBlock liveChat_date">'+msg.sendDate+'</div>'+
                             '</div>'+
                             '<div class="liveChat_infm">'+msg.text+'</div>'+
                         '</div>'+
                         
                     '</div>');
	    			alertInfo(msg.senderAlias+" "+msg.sendDate+"\n"+msg.text);
	    		}
	    		
	    		if(currentsessionid ==sessionid){
    				
    			}else{
    				if($('#'+sessionid+'_exist_info').length>0){
    					return;
    				}
    				$('#'+sessionid).append('<span class="liveChat_Message" id="'+sessionid+'_exist_info"></span>');
    			}
	    		$('#'+sessionid+'_message')[0].scrollTop = $('#'+sessionid+'_message')[0].scrollHeight;
			});
		}, function() {
			
		}, function() {
			
		});
	}
		
 
	 
	function keyPress(ev) {
		var sessionid =  $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		var currentInputMsg = $("#"+sessionid+"_edit_div").html();
		currentInputMsg = currentInputMsg.replace(/<(?!\/?BR|\/?img)[^<>]*>/ig, "");
		if (ev.ctrlKey) {
			
			var sendtype = $('.liveChat_enter').attr('id');
			if(sendtype == 'CtrlEnter'){
				// 发送数据
				sendMessageClick(currentInputMsg);
			} else {
				document.execCommand("paste");
				return false;
			}
		}
		if (ev.keyCode == 13) {
			var sendtype = $('.liveChat_enter').attr('id');
			if(sendtype == 'Enter'){
				// 发送数据
				sendMessageClick(currentInputMsg);
				return false;
			} else {
				document.execCommand("paste");
				return false;
			}
		}
		return true;
	}
	// 点击发送按钮发送
	function b_send() {
		var currentsession = $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		var currentInputMsg = $("#"+currentsession+"_edit_div").html();
		currentInputMsg = currentInputMsg.replace(/<(?!\/?BR|\/?IMG)[^<>]*>/ig, "");
		sendMessageClick(currentInputMsg);
	}
 
	
	/**
	 * 点击关闭时首先判断有几个选项卡 1.如果只有一个，需要根据存储的url（是从客户或者客服等待界面,url会不一样）字段进行跳转 客服-》等待界面
	 * 客户-》评分界面 2.如果是多个 找到当前选项卡的 index,销毁当前li(选项卡)与聊天窗体 找到下一个选项卡，置为可见状态
	 * 3.关闭后显示哪一个规则：往下推，如果此选项卡关闭了， 下面一个选项卡显示，如果是最后一个关闭，则显示上一个 4.在界面上移除选项卡
	 * 及发送请求到后台把此选项卡sessionid remove
	 */
	function closeChatRoom(t){
		var closeAlert = $("input[name='livechat_close_confirm']").val();
		var statu = confirm(closeAlert);
        if(!statu){
            return false;
        }
        
		var sessionid = $(t).parent().attr('id');
		
		var liArr = $("#sessionidlist ul li");
		var liNumber = liArr.length;
		if(liNumber == 1){
			// 跳转代码
			var roleId=$("input[name='livechat_roleId']").val();
			if(roleId==1){
				hangup(sessionid);
				$('#'+sessionid).remove();
				$('#'+sessionid+'_dialogue_window').remove();
				
				redirect('/livechat/customerServiceWaiting');
			}else if(roleId == 2){
				showScoreWindow();
			}
			
		}else{
			var currentIndex = 0;
			$(liArr).each(function(index,li){
				 if(li.id == sessionid){
					 currentIndex = index;
					 return false;
				 }
			});
			
			if(currentIndex+1 == liNumber){
				currentIndex = currentIndex-1;
			}else{
				currentIndex = currentIndex+1;
			}
			
			$(liArr).each(function(index,li){
				if(index == currentIndex){
					$('#'+li.id+'_dialogue_window').css('display','block'); 
					$(this).removeClass("none");
			        $(this).addClass("liveLeft_aci");
				}
			});
			hangup(sessionid);
			$('#'+sessionid).remove();
			$('#'+sessionid+'_dialogue_window').remove();
		}
		
		if(liNumber == 2){
			$("#sessionidlist").css('display','none'); 
			$('.liveChat_serviceRight').css('width','100%');
		}
	}
	
	
	$("#liveChat_close_common").click(function(){
		var liArr = $("#sessionidlist ul li");
		var liNumber = liArr.length;
		if(liNumber >1){
			var closeAllAlert = $("input[name='livechat_close_all_confirm']").val();
			var statu = confirm(closeAllAlert);
	        if(!statu){
	            return false;
	        }
		}else{
			var closeAlert = $("input[name='livechat_close_confirm']").val();
			var statu = confirm(closeAlert);
	        if(!statu){
	            return false;
	        }
		}
		
        var roleId=$("input[name='livechat_roleId']").val();
		if(roleId==1){
			//quit();
			$("#sessionidlist ul li").each(function(index,li){
				hangup(li.id);
			});
			redirect('/livechat/customerServiceWaiting');
		}else if(roleId == 2){
			showScoreWindow();
		}
	})  
 
	 
	function sendMessageClick(currentInputMsg){
		// 添加如果信息是空，或者全是空格，不发送信息判断
		
		// 此处需要获取ul-li的是哪一个高亮样式，然后再往哪一个发送信息
		  var sessionid =  $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		  if(currentInputMsg == ''){
			  var contentEmptyAlert = $("input[name='livechat_validate_content']").val();
			  alert(contentEmptyAlert);
			  return;
		  }
		  $('#'+sessionid+"_edit_div").empty();
		  
		  sendDataImpl(sessionid,currentInputMsg);
		
	}
	 

	function newchatroom(sessionid,otherAlias, ip, country){
		
		var flag = true;
		$("#sessionidlist ul li").each(function(index,li){
			var liclass = li.className;
			if(liclass=='liveLeft_aci'){
				flag =false;
				return false;
			}
		});
		
		if($("#"+sessionid).length>0){
			
		}else{
			if(flag){
				$("#sessionidlistUL").append("<li id='"+sessionid+"' value='"+sessionid+"' class='liveLeft_aci'>"+otherAlias+"<em class='close_liveChat' onclick='closeChatRoom(this)'></li>");
			}else{
				$("#sessionidlistUL").append("<li id='"+sessionid+"' value='"+sessionid+"' >"+otherAlias+"<em class='close_liveChat' onclick='closeChatRoom(this)'></li>");
			}
		}
		
		if($("#"+sessionid+'_dialogue_window').length>0){
			return false;
		}
		
		var current_activity_sessionid =  $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		
		var div_main = $('#'+current_activity_sessionid+'_dialogue_window').clone();
		 
		div_main.find('div[id^='+current_activity_sessionid+']').each(function(){
			var idAttr = $(this).attr('id');
			idAttr = idAttr.replace(current_activity_sessionid,sessionid);
			$(this).attr('id',idAttr)
		});
		
		div_main.attr('id',sessionid+'_dialogue_window');
		div_main.attr('class','liveChat_customer_con');
		div_main.attr('style','display: none;');
		div_main.find('.liveChat_dialogTitle').find('p').text('In conversation with '+otherAlias+'...');
		div_main.find('.liveChat_informationHB').find('p').text(otherAlias);
		
		div_main.find('#'+sessionid+'_message').empty();
		div_main.find('#'+sessionid+'_cip').find('span').text(ip);
		div_main.find('#'+sessionid+'_cfrom').find('span').text(country);
		
		
		$('#liveChat_customer_main').append(div_main);
		
		// 多个li时显示
		$("#sessionidlist").show(); 
		$('.liveChat_serviceRight').css('width','85%');
		/**
		 * 给新增加的li添加点击事件
		 */
		$("#sessionidlist ul li").click(function(){
			   
			var id =  $(this).attr('id');
			$("#sessionidlist ul li").each(function(index,li){
				 var sessionid = li.id;
				 $('#'+sessionid+'_dialogue_window').css('display','none'); 
				 $(this).removeClass("liveLeft_aci");
	             $(this).addClass("none");
			});
			 
			$('#'+id+'_dialogue_window').css('display','block'); 
			$(this).removeClass("none");
	        $(this).addClass("liveLeft_aci");
	        $('#'+id+'_exist_info').remove();
		})
		 
		/**
		 * 给新增的div聊天主界面设置宽度与高度
		 */
		getContentSize();
	}
	
	function getContentSize(){
		var winH = $(window).height();
		$(".liveChat_dialogBox").css({"height":winH-100})
		var writH = $(".liveChat_dialogBox").height();
		$(".liveChat_dialogShow").css({"height":writH-33})
		$(".liveChat_serviceLeft").css({"height":winH-67})
	}
	
	function showScoreWindow(){
		$('#score_window').css('display','block');
	} 
	
	function saveScore(){
		var scroes = {};
		$("div[id^='divquestion_']").each(function(i){
			var key = $(this).attr('alt');
			var value = $(this).attr('class');
			value = value.charAt(value.length - 1);
			scroes[key]=value;
		});
		
		var tsessionid = $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		var score = {
			 sessionId : tsessionid,
			 email : $("#score_email").val(),
			 comment : $("#score_descriptions").val(),
			 questionScore : scroes,
		};
		
		$.ajax({
			   type: "POST",
			   url: "/livechat/score",
			   contentType:"application/json",
			   data: JSON.stringify(score) ,
			   success: function(msg){
			   	 hangup(tsessionid);
			   	 DispClose = false;
			   	 window.close();
			   }
			});
		}   
	
	$("#score_submit").click(function(){
		saveScore();
	});
	
	var needalertinfo = true; 
	function alertInfo(msg){
		if(needalertinfo==false){
			return;
		}
		if (window.Notification){
	        if(Notification.permission==='granted'){
	            var iconrul = $("img[class=liveChat_informationH]").attr('src');
	            var notification = new Notification('livechat info',{tag:"info",body:msg,icon:iconrul});
	            notification.onclick = function(){$(window).focus();};
	        }
        }
	}
	
	$(window).bind('blur', this.windowBlur).bind('focus', this.windowFocus);   
	
	function windowBlur(){
		needalertinfo = true;
	}
	function windowFocus(){
		needalertinfo = false
	}
	
	$("#livechat_message_history").click(function(){
		var sessionId = $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		window.open('/sysadmin/livechat/history?sessionId='+sessionId,'LiveChatHistory','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,width=1250,height=470,left=180,top=140');
	})
	
	// Transfer
	function transfer(csid) {
		var tsessionid = $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		var ctl = {
			type : "TRANSFER",
			id : tsessionid,
			to : csid
		};
		sendControl(ctl);
	}
	
	$("li .liveYes").click(function(){
		transfer($(this).attr("tag"));
		var tsessionid = $("#sessionidlist ul li[class='liveLeft_aci']").attr('id');
		$('#'+tsessionid+'_message').append("<p>transfer to "+$(this).attr("tag") +"</p>");
		$(this).parent().css("display","none");
	});
	
	//
	$("#liveChat_transfer").click(function(){
		if($("#liveChat_transfer_users").css("display")=="none"){
			return;
		}
		$.ajax({
			   type: "GET",
			   dataType: "json",
			   url: "/livechat/cs/status",
			   success: function(data){
				   $.each(data, function(i, d) {
					 var liobj =  $("li[name="+d.alias+"]");
					 liobj.children("div .liveCh_B").unbind("click");
					 if(d.self){
						 if(liobj.hasClass("liveChat_sRed")==false){
							 liobj.addClass("liveChat_sRed");
						 }
						 if(liobj.children().hasClass("liveCh_B")==true){
							 liobj.children().removeClass("liveCh_B");
						 }
					 }else{
						 if(d.active){
							 if(liobj.hasClass("liveChat_sGreey")==true){
								 liobj.removeClass("liveChat_sGreey");
							 }
							 if(liobj.hasClass("liveChat_sRed")==true){
								 liobj.removeClass("liveChat_sRed");
							 }
							 if(liobj.children().hasClass("liveCh_B")==false){
								 liobj.children().addClass("liveCh_B");
							 }
						     liobj.children("div .liveCh_B").click(function(){
								 if($(this).next(".liveChatPop").css("display")=="none"){
									 $(this).next(".liveChatPop").css("display","block")
								 }else{
									 $(this).next(".liveChatPop").css("display","none")
								 }
							 });
						 }else{
							 if(liobj.hasClass("liveChat_sGreey")==false){
								 liobj.addClass("liveChat_sGreey");
							 }
							 if(liobj.children().hasClass("liveCh_B")==true){
								 liobj.children().removeClass("liveCh_B");
							 }
						 }
					 }
				   });//end each
			   }
		});
	});

 
