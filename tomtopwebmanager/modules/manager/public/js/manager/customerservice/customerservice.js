define([ 'jquery', 'jqueryjson' ], function($) {
	$(document).ready(function() {
		require(['bootstrap-datetimepicker'], function(){
			$('#start_date').datetimepicker({
				format : "yyyy-mm-dd hh:ii",
				autoclose : true
			}).on('changeDate', function(ev) {
				var startTime = $('#start_date').val();
				$('#end_date').datetimepicker('setStartDate', startTime);
				$('#start_date').datetimepicker('hide');
			});
			
			$('#end_date').datetimepicker({
				format : "yyyy-mm-dd hh:ii",
				autoclose : true
			}).on('changeDate', function(ev) {
				var startTime = $('#end_date').val();
				$('#start_date').datetimepicker('setEndDate', startTime);
				$('#end_date').datetimepicker('hide');
			});
			
			$('#search_startDate').datetimepicker({
				minView: "month",
				maxView: "year",
				format : "yyyy-mm-dd",
				autoclose : true
			}).on('changeDate', function(ev) {
				var startTime = $('#search_startDate').val();
				$('#search_endDate').datetimepicker('setStartDate', startTime);
				$('#search_startDate').datetimepicker('hide');
			});
			
			$('#search_endDate').datetimepicker({
				minView: "month",
				maxView: "year",
				format : "yyyy-mm-dd",
				autoclose : true
			}).on('changeDate', function(ev) {
				var startTime = $('#search_endDate').val();
				$('#search_startDate').datetimepicker('setEndDate', startTime);
				$('#search_endDate').datetimepicker('hide');
			});
		});
	});
	
	$("a.edit").click(function() {
		var id = $(this).attr("data-target");
		var href = $(this).attr("href");
		var body = $(id).find("div.modal-body");
		body.html("");
		$.get(href, function(html) {
			body.html(html);
		}, "html");
	});
	
	$("#search_form").find("input[type=submit]").click(function(){
		$(this).find("input[name=p]").val("1");
	});
	
	$("#search_page").find("a[tag=pageNum]").click(function(){
		var p = $(this).attr("value");
		$("#search_form").find("input[name=p]").val(p);
		$("#search_form").submit();
	});
});