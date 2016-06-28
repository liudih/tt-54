package controllers;

import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.inject.Inject;

import controllers.base.Home;

public class Application extends Controller {

	@Inject
	Home home;

	public Promise<Result> index() {
		return home.home();
	}

}
