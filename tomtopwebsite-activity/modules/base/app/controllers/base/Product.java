package controllers.base;

import play.mvc.Controller;
import play.mvc.Result;

public class Product extends Controller {

	public Result view(String title) {
		String host = play.Play.application().configuration()
				.getString("main_website");
		if (title == null && title.length() == 0) {
			return play.mvc.Results.redirect(host);
		} else {
			return play.mvc.Results.redirect(host + "/" + title + ".html");
		}
	}
}
