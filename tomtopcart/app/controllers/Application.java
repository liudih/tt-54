package controllers;

import java.util.List;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {


	public Promise<Result> index() {
//		TemplateComposite contents = TemplateService.getInstance()
//				.getContents();
		List<String> showFragmentIds = Play.application().configuration().getStringList("showFragmentIds");
		Logger.debug("showFragmentIds:{}",showFragmentIds);
		
		return Promise.pure(redirect("/cart"));
	}

}
