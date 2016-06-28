package controllers.help;

import play.mvc.Controller;
import play.mvc.Result;
import services.base.utils.MetaUtils;

public class Help extends Controller {
	public Result points() {
		MetaUtils.currentMetaBuilder().setTitle("TOMTOP Points");
		return ok(views.html.help.tomtopPoints.render());
	}

	public Result dropShipping() {
		MetaUtils.currentMetaBuilder().setTitle("Dropship");
		return ok(views.html.help.dropShipping.render());
	}

	public Result affiliates() {
		MetaUtils.currentMetaBuilder().setTitle("Affiliates");
		return ok(views.html.help.affiliates.render());
	}
	
	public Result viplevels() {
		MetaUtils.currentMetaBuilder().setTitle("VipLevels");
		return ok(views.html.help.vipLevel.render());
	}
}
