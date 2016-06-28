package controllers.manager;

import play.mvc.Controller;
import play.mvc.Result;

public class Tool  extends Controller {
	public Result toPwiki() {
		return redirect("http://piwik.tomtop.com/");
	}

}
