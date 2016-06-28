$(function() {
	$('.datepicker').datetimepicker({
		format : 'dd/mm/yyyy',
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		minView : 2,
		forceParse : 0
	});
	
	var handlerTable = function(json, target) {
		var $table = $(target);
		var html = '';
		$table.find('tbody').html('');
		for ( var i in json.result.list) {
			var o = json.result.list[i];
			html += '<tr>' + '<td>' + o.ordernumber + '</td>' + '<td>' + o.date
					+ '</td>' + '<td>' + o.sku + '</td>' + '<td>$' + o.sale
					+ '</td>' + '<td>$' + o.commission + '</td>' + '<td>'
					+ o.ordersate + '</td>' + '<td>' + o.traffic + '</td>'
					+ '<td>' + o.commissionstae + '</td>' + '</tr>';
		}

		$table.find('tbody').append(html);
		var id = json.id;
		pageTag(id, json.result.page, json.result.pageTotal)
		detailTotal(id, json.result.saleTotal, json.result.commissionTotal);
		var page = page || {};
		page.pageNo=json.result.page;
		page.size=json.result.pageSize;
		page.total=json.result.pageTotal;
		page.totalCount=json.result.totalCount;
		var list=json.result.list;
		page.record=list==null?0:list.length;
		showPageInfo(json.id,page);
	}

	function detailTotal(id, saleTotal, commissionTotal) {
		$('#' + id + '_saleTotal').html(saleTotal);
		$('#' + id + '_commission').html(commissionTotal);
	}

	function pageTag(id, pageNo, pages) {
		$('#' + id + 'pagination').pagination(
				{
					pages : pages,
					currentPage : pageNo,
					hrefTextPrefix : 'javascript:void(',
					hrefTextSuffix : ')',
					cssStyle : 'light-theme',
					selectOnClick : true,
					onPageClick : function(pageNumber, event) {
						$('#' + id + '_table_form').find('input[name=page]')
								.val(pageNumber);
						$('#' + id + '_table_form').submit();
					}
				});
	}

	var $loading = $('<div class="loding" style="position: relative;height:30px;"><div class="throbber" style="display:block;  position: absolute;left: 50%;top: 50%;margin-left: -8px;margin-top: -8px;"></div></div>');
	var ajaxLoadDetail = function(tableform) {
		var $form = $(tableform)
		var url = $form.attr('action');
		var target = $form.find('input[name=target]').val();
		$form.ajaxSubmit({
			type : 'post',
			url : url,
			beforeSubmit : function() {
				$(target).find('tbody').empty();
				$(target).after($loading);
			},
			error : function() {

			},
			success : function(data, status, xhr) {
				$(target).next('.loding').remove();
				handlerTable(data, target);
			}

		});
		return false;
	}
	$('.table-form').each(function(i, e) {
		ajaxLoadDetail(this)
	})

	$('.table-form').each(function(i, e) {
		$(this).on('submit', function() {
			ajaxLoadDetail(this, i);
			return false;
		});
	});

	$('.select-status').each(function() {
		$(this).on('change', function() {
			var form = $(this).data('form');
			$(form).find('input[name=orderStatus]').val($(this).val());

			$(form).find('input[name=page]').val(1);
			ajaxLoadDetail(form)
		})
	});

	$('.comminsonStatus').each(function() {
		$(this).on('change', function() {
			var form = $(this).data('form');
			$(form).find('input[name=comminsonStatus]').val($(this).val());
			$(form).find('input[name=page]').val(1);
			ajaxLoadDetail(form)
		})
	});

	var $trafficTableForm = $('#traffic_table_form');
	var $tab = $('#traffic_table');
	function trafficTab(json) {
		$tab.find('tbody').html('');
		var html = '';
		if (json.data.list && json.data.list.length > 0) {
			for ( var i in json.data.list) {
				var d = json.data.list[i];
				html += '<tr><td>' + d.csource + '</td><td>' + d.cpath
						+ '</td></tr>'
			}
		}
		$tab.find('tbody').append(html);
		pageTag(json.id, json.data.page, json.data.pageTotal)
		$('#' + json.id + "_clicks").html(json.data.clickTotal)
		$('#' + json.id + "_unique_clicks").html(json.data.unclickTotal);
		var page = page || {};
		page.pageNo=json.data.page;
		page.size=json.data.pageSize;
		page.totalCount=json.data.totalCount;
		page.record=json.data.list.length;
		showPageInfo(json.id,page);
		
	}
    function showPageInfo(tagId,page){
    	var text='';
    	if(page.totalCount==0){
    		text+='0 record'
    	}else{
    		var p=(page.pageNo-1)*(page.size);
        	var from=p+1,
            record=page.record;
        	to=p+record,
        	total=page.totalCount;
        	text+='Showing'+from+'-'+to+' of '+total+' total'
    	}   	
    	$('#'+tagId+"-page-num-info").html(text);
    }
	function ajaxLoadTraffic($form) {
		var url = $form.attr('action');
		$form.ajaxSubmit({
			type : 'post',
			url : url,
			beforeSubmit : function() {
				$tab.find('tbody').empty();
				$tab.after($loading);
			},
			error : function() {
			},
			success : function(data, status, xhr) {
				$tab.next('.loding').remove();
				trafficTab(data);
			}
		});
	}

	$trafficTableForm.on('submit', function() {
		ajaxLoadTraffic($(this));
		return false;
	});
	$trafficTableForm.submit();
	$('a[data-toggle]').on('click', function(e) {
		var form = $(this).data('form');
		var $form = $(form);
		var pageSize = $(this).data('value');
		$form.find('input[name=pageSize]').val(pageSize);
		$form.find('input[name=page]').val(1);
		$form.submit();
		var tag = $(this).data('class');
		$(tag).removeClass('chartSA');
		$(this).addClass('chartSA');
		e.preventDefault();
	});

});
