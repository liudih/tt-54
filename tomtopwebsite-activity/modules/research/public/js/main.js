function scrollLikes() {
	var winW = $(document).width();
	var sitTop = $(".contentWarp").offset().top;
	var scrollTop = document.documentElement.scrollTop
			|| document.body.scrollTop;
	if (scrollTop > sitTop) {
		$(".likesFix").css({
			"position" : "fixed",
			"top" : "10px"
		});
	} else if (winW < 1300 && scrollTop < sitTop) {
		$(".likesFix").css({
			"position" : "absolute",
			"right" : "0px",
			"top" : "0"
		});
	} else {
		$(".likesFix").css({
			"position" : "absolute",
			"top" : "0"
		});
	}
}
function winSize() {
	var winW = $(document).width();
	var sitTop = $(".contentWarp").offset().top;
	var scrollTop = document.documentElement.scrollTop
			|| document.body.scrollTop;
	if (winW < 1300) {
		$(".likesFix").css({
			"position" : "fixed",
			"right" : "0px",
			"top" : sitTop
		});
		$(".likesAbs").css({
			"right" : "0px"
		});
	} else {
		$(".likesFix").css({
			"position" : "absolute",
			"right" : "auto"
		});
		$(".likesAbs").css({
			"right" : "-50px"
		});
	}
}
$(function() {
	function errorPop() {
		var params = {
			content : 'One vote per account per day!'
		};
		params = $.extend({
			'position' : {
				'zone' : 'center'
			},
			'overlay' : true
		}, params);
		var markup = [ '<div class="errorPop_Box">',
				'<div class="checkbox"></div>', '<p class="checkTxt">',
				params.content, '</p></div>' ].join('');
		$(markup).hide().appendTo('body').fadeIn();
		setTimeout(function() {
			$('.errorPop_Box').fadeOut(function() {
				$('.errorPop_Box').remove()
			});
		}, 1000);
	}
	function successful() {
		var params = {
			content : 'Vote successful!'
		};
		params = $.extend({
			'position' : {
				'zone' : 'center'
			},
			'overlay' : true
		}, params);
		var markup = [ '<div class="successfulPop_box">',
				'<div class="checkbox"></div>', '<p class="checkTxt">',
				params.content, '</p></div>' ].join('');
		$(markup).hide().appendTo('body').fadeIn();
		setTimeout(function() {
			$('.successfulPop_box').fadeOut(function() {
				$('.successfulPop_box').remove()
			});
		}, 1000);
		$(".votesGra").click(function() {
			errorPop();
		})
	}
	$(".votesClick").click(function() {
		var data = {};
		data.activityType = $("#votePageType").val();
		data.activityPageId = $("#votePageID").val();
		data.endDate = $("#voteEndDate").val();
		data.startDate = $("#voteBeginDate").val();
		data.activityPageItemId = $(this).attr("id");
		$.ajax({
			url : "/research/hothit",
			type : "post",
			data : $.toJSON(data),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				$(this).addClass("votesGra");
				if (data.fixedStatus == "NO_LOGIN") {
					$(".blockPopup_box").show();
				} else if (data.fixedStatus == "SUCC") {
					$("#itemCount" + data.pateItemid).text(data.count);
					successful();
				} else if (data.fixedStatus == 'EXOURE') {
				} else if (data.fixedStatus == 'VIOLATION') {
					errorPop();
				} else {
					errorPop();
				}
			}
		});
	})
	winSize();
})
$(window).scroll(function() {
	scrollLikes()
})
$(window).resize(function() {
	winSize()
})