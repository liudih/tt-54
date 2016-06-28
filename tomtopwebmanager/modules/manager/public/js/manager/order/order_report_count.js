define([ 'jquery', 'jqueryjson' ], function($) {
	$(document).ready(
			function() {
				require([ 'jquery', 'bootstrap-datetimepicker' ], function() {

					var param = {
						minView : "month",
						maxView : "year",
						format : "yyyy-mm-dd",
						autoclose : true
					};
					var param2 = {
						minView : "month",
						maxView : "year",
						format : "yyyy-mm",
						autoclose : true
					};
					$('#orderdatestart').datetimepicker(param);
					$('#orderdatestart2').datetimepicker(param2);
					$('#orderdatestart3').datetimepicker(param);
					$('#orderdateend').datetimepicker(param);
					$('#orderdateend2').datetimepicker(param);

					$('#orderdatestart').datetimepicker({}).on(
							'changeDate',
							function(ev) {
								var startTime = $(this).val();
								$('input[name=endDate]').datetimepicker(
										'setStartDate', startTime);
							});

					$('#orderdatestart2').datetimepicker({}).on('changeDate',
							function(ev) {
								var startTime = $('.orderdatestart').val();
								$('#orderdatestart2').datetimepicker('hide');
							});

					$('#orderdatestart3').datetimepicker({}).on(
							'changeDate',
							function(ev) {
								var startTime = $(this).val();
								$('input[name=endDate]').datetimepicker(
										'setStartDate', startTime);
							});
				});

				var siteId = $("#siteId").val();
				if (siteId != "") {
					$("#webId").val(siteId);
				}
				var type = $("#type").val();
				if (type != "") {
					$("#dateId").val(type);
					var vs = $("#dateId").val();
					if (vs == 1) {
						$("#orderdatestart2").val('');
						$("#orderdatestart3").val('');
						$("#orderdateend2").val('');
						$("#date").show();
						$("#month").hide();
						$("#dateRange").hide();
					} else if (vs == 2) {
						$("#orderdatestart").val('');
						$("#orderdatestart3").val('');
						$("#orderdateend2").val('');
						$("#orderdateend").val('');
						$("#date").hide();
						$("#month").show();
						$("#dateRange").hide();
					} else if (vs == 3) {
						$("#orderdatestart").val('');
						$("#orderdatestart2").val('');
						$("#orderdateend").val('');
						$("#date").hide();
						$("#month").hide();
						$("#dateRange").show();
					}
				}
			});

	$("#search").click(function() {
		var vs = $("#dateId").val();
		if (vs == 1) {
			var startDate = $("#orderdatestart").val();
			var endDate = $("#orderdateend").val();
			var aDate, oDate1, oDate2, iDays
			aDate = startDate.split("-")
			oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]) // 转换为12-18-2006格式
			aDate = endDate.split("-")
			oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0])
			iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24) // 把相差的毫秒数转换为天数
			if (startDate != "" && endDate != "") {
				if (iDays > 31) {
					alert("起止时间不能超过31天！");
					return false;
				}
			}
			return true;
		}

	});

});