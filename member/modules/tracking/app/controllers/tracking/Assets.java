package controllers.tracking;

import play.api.mvc.Action;
import play.api.mvc.AnyContent;
import play.mvc.Controller;

public class Assets extends Controller {

	public static Action<AnyContent> at(final String path, final String file) {
		return controllers.Assets.at(path, file, false);
	}
}
