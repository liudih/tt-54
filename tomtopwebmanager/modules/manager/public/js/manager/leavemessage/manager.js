$(function() {

	$(".leave_message_operate").click(function() {
		var iid = this.id;
		if(!confirm("确定要将状态修改为已处理吗？")){
			return;
		}
		$.ajax({
			url : '/sysadmin/leavemessage/handle/' + iid,
			type : 'POST',
			success : function(data) {
				if (data.errorCode === 0) {
					alert('This message has been processed！');
					$('#leave_message_search_btn').click();
				} else if (data.errorCode === 1) {
					alert('Handle success!');
					$('#leave_message_search_btn').click();
				}

			}
		});
	})

	var pageNo = $("input[name='pageNo']").val();
	var totalPages = $("input[name='totalPages']").val();

	// 初始化所需数据
	var options = {
		bootstrapMajorVersion : 3,// 版本号。3代表的是第三版本
		currentPage : pageNo, // 当前页数
		numberOfPages : 10, // 显示页码数标个数
		totalPages : totalPages, // 总共的数据所需要的总页数
		itemTexts : function(type, page, current) {
			// 图标的更改显示可以在这里修改。
			switch (type) {
			case "first":
				return "first";
			case "prev":
				return "prev";
			case "next":
				return "next";
			case "last":
				return "last";
			case "page":
				return page;
			}
		},
		tooltipTitles : function(type, page, current) {
			// 如果想要去掉页码数字上面的预览功能，则在此操作。例如：可以直接return。
			switch (type) {
			case "first":
				return "Go to first page";
			case "prev":
				return "Go to previous page";
			case "next":
				return "Go to next page";
			case "last":
				return "Go to last page";
			case "page":
				return (page === current) ? "Current page is " + page
						: "Go to page " + page;
			}
		},
		onPageClicked : function(e, originalEvent, type, page) {
			var ilanguageid = $("#ilanguageid_search  option:selected").val();
			var itopicid = $("#leave_message_topic  option:selected").val();
			var bishandle = $("#leave_message_bishandle  option:selected")
					.val();
			var pid = $("#leave_message_p_user  option:selected").val();
			var handlerid = $("#leave_message_handle  option:selected").val();

			// 单击当前页码触发的事件。若需要与后台发生交互事件可在此通过ajax操作。page为目标页数。
			location.href = js_LeaveMessageRoutes.controllers.manager.LeaveMessage
					.searchLeaveMessagePage(page, ilanguageid, itopicid,
							bishandle,pid,handlerid).url;
		}
	};
	var element = $('#leave-message-paginator');// 获得数据装配的位置
	element.bootstrapPaginator(options); // 进行初始化

	$(".leave_msg_replay_win").click(
			function() {
				var id = $(this).parent().parent().parent().attr('id');
				if(id==$("#leave_msg_reply_title").attr("tag")){
					return;
				}
				$("#leave_msg_reply_title").attr("tag",
						$(this).parent().parent().parent().attr('id'));
				CKEDITOR.instances['leave_msg_reply_content'].setData("");
				$("#leave_msg_reply_title").val("");
	});
	
	var pretime=0;
	$("#leave_msg_replay_win .modal-footer button[type=submit]").click(function() {
		if((new Date().getTime()-pretime)<2000){
			return;
		}
		pretime = new Date().getTime();
		var id = $("#leave_msg_reply_title").attr("tag");
		var con = CKEDITOR.instances['leave_msg_reply_content'].getData();
		if(con==""||con==null){
			alert("描述不能为空!");
			return;
		}
		var contents={
			leaveMsgId: id,
			title: $("#leave_msg_reply_title").val(),
			content: con
		};
		$.ajax({
			url : '/sysadmin/leavemessage/reply/email',
			type : 'POST',
			contentType:'application/json',
			data:JSON.stringify(contents),
			success : function(data) {
				alert("sended");
			},
			error : function(data){
				if(data.responseText){
					alert('failed :' +JSON.stringify(data.responseText));
				}else{
					alert('failed :' +JSON.stringify(data));
				}
			}
		});
	});
	
	
	$(".leave_msg_replay_win_message").click(
			function() {
				var id = $(this).parent().parent().parent().attr('id');
				if(id==$("#leave_msg_replay_win_message").attr("tag")){
					return;
				}
				
				$("#leave_msg_replay_win_message").attr("tag",id);
				$("#leave_msg_replay_win_message input[name=cemail]").val($(this).parent().parent().parent().attr('tag'));
				$("#leave_msg_replay_win_message input[name=subject]").val('');
				$("#leave_msg_replay_win_message textarea[name=content]").val('');
	});
	
	$("#leave_msg_replay_win_message .modal-footer button[id=add-user-btn]").click(function() {
		if((new Date().getTime()-pretime)<2000){
			return;
		}
		pretime = new Date().getTime();
		var id = $("#leave_msg_reply_title").attr("tag");
		var email = $("#leave_msg_replay_win_message input[name=cemail]").val();
		var title =$("#leave_msg_replay_win_message input[name=subject]").val();
		var content =$("#leave_msg_replay_win_message textarea[name=content]").val();
		if(email==""||email==null){
			alert("email不能为空!");
			return;
		}
		if(title==""||title==null){
			alert("title不能为空!");
			return;
		}
		if(content==""||content==null){
			alert("content不能为空!");
			return;
		}
		var contents={
			leaveMsgId:id,
			email: email,
			title: title,
			content: content
		};
		$.ajax({
			url : '/sysadmin/leavemessage/reply',
			type : 'POST',
			contentType:'application/json',
			data:JSON.stringify(contents),
			success : function(data) {
				alert("sended");
			},
			error : function(data){
				if(data.responseText){
					alert('failed :' +JSON.stringify(data.responseText));
				}else{
					alert('failed :' +JSON.stringify(data));
				}
			}
		});
	});
	

});