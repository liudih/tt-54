$(function(){
	$('#badgeContent').autobrowse(
        {
        	page : 1,
        	
            url: function (offset)
            {
            	var urlStr;
            	if(!window.moreurl){
					if($.isFunction(window.getScrollLoadUrl)){
						urlStr = getScrollLoadUrl(this.page+1);
					}
				}else{
					urlStr = moreurl
				}
            	if(urlStr.indexOf('?') == -1){
            		return urlStr + '?p=' + (this.page+1) + '&ismore=1';
            	}else{
            		return urlStr + '&p=' + (this.page+1) + '&ismore=1';
            	}
                
            },
            template: function (response)
            {
            	this.page = this.page + 1 ;
            	this.totlePage = response.totlePage || 1;
            	
            	return   $.parseHTML(response.html);
				
				 
				
            },
            itemsReturned: function (response) { 
            	return 1; 
            },
            complete: function (response) {
            	 $(".blackPop,.iconClose,.li_clickPop li").click(function(){
            		 $(".popBoxNone").hide();
            		 $(".popBoxClick").removeClass("subCate_titleSj");
            	 });
            	 imgHeight();
            },
			stopFunction: function () {
        		if(this.totlePage && this.page > this.totlePage){
        			return true;
        		}
        		return false;
        	},
        	
            offset: 0,
            max: 100,
            loader: '<div  class="loading_button" style="display:block;"><img src="/base/assets/images/icon/loading.gif"></div>',
            useCache: false,
            expiration: 1
        }
    );
	
});