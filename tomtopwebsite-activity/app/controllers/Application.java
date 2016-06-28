package controllers;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;

import controllers.base.Home;
import controllers.research.Vote;
import interceptors.CacheResult;
import play.*;
import play.libs.F.Promise;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	@Inject
	Home home;
	
	@Inject
	Vote vote;

	public Promise<Result> index() {
		String host = play.Play.application().configuration()
				.getString("main_website");
		return Promise.pure(Results.redirect(host));// home.home();
	}
	
	public Promise<Result> viewPath(String title) {
		return Promise.pure((vote.view(title)));// home.home();
	}

	public Promise<Result> rindex(String path) {
		String host = play.Play.application().configuration()
				.getString("main_website");
		return Promise.pure(Results.redirect(host));// home.home();
	}

	public Result robots() {
		String str = getRobotsTxt();
		return ok(str).as("text/plain");
	}

	@CacheResult
	private String getRobotsTxt() {
		String str = "";
		try {
			str = IOUtils.toString(Application.class.getClassLoader()
					.getResourceAsStream("robots.txt"));
		} catch (IOException e) {
			Logger.info("read robots.txt file error");
		}
		return str;
	}

	public Result sitemap() {
		return ok("");
	}

}
