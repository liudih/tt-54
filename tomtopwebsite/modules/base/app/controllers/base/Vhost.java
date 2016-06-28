package controllers.base;

import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;

import com.google.inject.Inject;

public class Vhost extends Controller {
	@Inject
	FoundationService foundationService;
	public Result getVhost() {
		String vhost = foundationService.getVhost();
		return ok(vhost);
	}
}
