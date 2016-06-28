
$("form").submit(function(e) {
	var src = document.activeElement;
	var form = $("#" + src.name);
	console.info(form.serialize());
	var url = form.attr("action");
	$.post(url, form.serialize(), function(data) {
		if (data['result'] == true) {
			alert('保存成功!');
		} else {
			alert('保存失败!');
		}
	}, "json");
	return false;
});
