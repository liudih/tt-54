$(function() {
	search();
	saveDiscount();
});

function search() {
	$("#search_form").submit(function() {
		searchSubmit();
		return false;
	});
}

function searchSubmit() {
	var form = $("#search_form");
	var url = form.attr("action");
	var data = form.serialize();
	$.post(url, data, function(h) {
		$("#discount_table").replaceWith(h);
		a_action();
	}, "html");
}

function a_action() {
	$("a[data-toggle=modal]").click(function() {
		var id = $(this).attr("data-target");
		var href = $(this).attr("href");
		var body = $(id).find("div.modal-body");
		body.html("");
		$.get(href, function(html) {
			body.html(html);
		}, "html");
	});
	$("a.delete").click(function() {
		var url = $(this).attr("value");
		var tr = $(this).parents("tr");
		$.get(url, function() {
			tr.remove();
		});
		return false;
	});
}

function saveDiscount() {
	$("form[name=save_discount]").submit(function() {
		var form = $(this);
		var url = form.attr("action");
		var data = form.serialize();
		$.post(url, data, function(data) {
			if (data.res) {
				searchSubmit();
				alert("Successful");
			} else {
				alert("Failure");
			}
		}, "json");
		return false;
	});
}