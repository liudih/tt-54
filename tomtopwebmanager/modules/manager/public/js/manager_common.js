//分页公用方法
function paginationCommon(pageNo,totalPages,formId,divId){
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
	    	$("#p").val(page);
	    	$("#"+formId)[0].submit();
	    }
	};
	var element = $('#'+divId);//获得数据装配的位置
	element.bootstrapPaginator(options);	//进行初始化
}
