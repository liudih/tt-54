package controllers;

import java.net.MalformedURLException;

import javax.inject.Inject;

import controllers.base.Home;
import play.Logger;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.F.Promise;

public class Application extends Controller {

	@Inject
	Home home;

	public Promise<Result> index() {
		return Promise.pure(home.home());
	}
}
