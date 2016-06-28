define([ 'jquery' ], function() {

	function ModelTopicPage() {
	}

	ModelTopicPage.prototype = {
		getFormData : function(e) {
			var form = {};
			var type = $(e).parent().attr("type");
			var tag = $(e).attr("tag");
			form["type"] = type;
			$("div[name=form_div]>span.spanActive[tag=" + tag + "]").each(
					function() {
						var name = $(this).attr("name");
						var value = $(this).attr("value");
						form[name] = value;
					});
			return form;
		},

		ajaxRefresh : function(e) {
			var formData = this.getFormData(e);
			var url = collectionRoute.controllers.topic.Topic.refresh().url;
			$.post(url, formData, function(html) {
				var id = "pageList_" + formData["type"];
				id = id.replace(/\ +/g, "");
				$("#" + id).replaceWith(html);
			}, "html");
		}
	}

	return ModelTopicPage;

});