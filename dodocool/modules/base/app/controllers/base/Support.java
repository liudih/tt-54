package controllers.base;

import play.mvc.Controller;
import play.mvc.Result;

public class Support extends Controller {

	public Result home() {
		return ok(views.html.base.support.support.render());
	}
}
