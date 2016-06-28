package controllers;

import org.apache.commons.lang3.RandomStringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

public class Home extends Controller {


	public static Promise<Result> notFoundPromiseResult() {
		return Promise.pure(notFoundResult());
	}

	public static Promise<Result> errorPromiseResult(RequestHeader request,
			Throwable t) {
		return Promise.pure(errorResult(request, t));
	}

	public static Result notFoundResult() {
		String header = request().getHeader("Referer");
		return notFound(views.html.not_found.render(header));
	}

	public static Result errorResult(RequestHeader request, Throwable t) {
		String errorCode = RandomStringUtils.random(8,
				"abcdefghijkmnpqrstuvwxyz23456789");
		if (t != null && t.getCause() != null) {
			Logger.error("Further cause", t.getCause());
		}
		return internalServerError(views.html.error.render(request, t,
				errorCode));
	}
}
