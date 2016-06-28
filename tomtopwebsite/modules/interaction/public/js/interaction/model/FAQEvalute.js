define(
	['jquery'],
	function() {
		function FAQEvalute(){
		}
		var saveError = -3;		//保存错误
		var notLogin = -1;		//未登录
		var alreadyExist = 0;	//会员已提交过
		var saveSuccess = 2;	//保存成功
		FAQEvalute.prototype = {

			addEvalute: function(e) {
				var faqId = $(e).attr("tag");
				var isHelpful = $(e).attr("param");
				var addEvaluteRoute = evaluteRoutes.controllers.interaction.InteractionFAQ.addEvalute(faqId, isHelpful);
				$.get(addEvaluteRoute.url, function(json) {
					var state = json.state;
					if (notLogin == state) {
						var loginRoute = loginRoutes.controllers.member.Login.loginForm();
						alert("Please log in!");
						window.open(loginRoute.url);
					} else if (saveSuccess == state) {
						var count = json.count;
						var span = $(e).next("span").text(count);
					} else if (alreadyExist == state) {
						alert("You have already commented on this question!");
					} else if (saveError == state) {
						alert("Unkown error!");
					}
				}, "json");
			}
		}
		return FAQEvalute;
	}
);