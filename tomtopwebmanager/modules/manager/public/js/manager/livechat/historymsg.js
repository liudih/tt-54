$(function(){
   
	
	 $('#histsory_msg_datestart').datetimepicker({
		　   format: 'yyyy-mm-dd',  //选择日期后，文本框显示的日期格式 
		         weekStart: 1,  
		         autoclose: true,  
		         startView: 2,  
		         minView: 2,  
		         forceParse: false
		  	
		  }).on('changeDate', function(ev){
			  var startTime = $('#orderdatestart').val();
			  $('#histsory_msg_dateend').datetimepicker('histsory_msg_datestart', startTime);
			  $('#histsory_msg_datestart').datetimepicker('hide');
		  });
	 $('#histsory_msg_dateend').datetimepicker({
		　　format: 'yyyy-mm-dd',  //选择日期后，文本框显示的日期格式 
	         weekStart: 1,  
	         autoclose: true,  
	         startView: 2,  
	         minView: 2,  
	         forceParse: false
		  });
	
	var pageNo = $("input[name='pageNo']").val();
	var totalPages = $("input[name='totalPages']").val();
   
    //初始化所需数据
    var options = {
        bootstrapMajorVersion:3,//版本号。3代表的是第三版本
        currentPage: pageNo, //当前页数
        numberOfPages: 10, //显示页码数标个数
        totalPages:totalPages, //总共的数据所需要的总页数
        itemTexts: function (type, page, current) {  
        //图标的更改显示可以在这里修改。
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
                    return  page;  
            }                 
        }, 
        tooltipTitles: function (type, page, current) {
			//如果想要去掉页码数字上面的预览功能，则在此操作。例如：可以直接return。
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
	                return (page === current) ? "Current page is " + page : "Go to page " + page;
	        }
        },
        onPageClicked: function (e, originalEvent, type, page) {
        	var sDate = $("input[name='startDate']").val();
        	var eDate = $("input[name='endDate']").val();
        	var sName = $("input[name='customerServiceName']").val();
        	var cName = $("input[name='customerName']").val();
        	var key = $("input[name='keyword']").val();
        	
            //单击当前页码触发的事件。若需要与后台发生交互事件可在此通过ajax操作。page为目标页数。
      		location.href = js_HistoryMessageRoutes.controllers.manager.livechat.LiveChat.searchHistoryMsgPage(page,cName,sName,key, sDate, eDate).url;
        }
    };
    var element = $('#history-message-paginator');//获得数据装配的位置
    element.bootstrapPaginator(options);	//进行初始化
     
	
});