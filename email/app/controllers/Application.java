package controllers;

import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public Promise<Result> index() {
		return Promise.pure(redirect("/email"));
	}

}
