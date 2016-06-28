package controllers;

import interceptors.CacheResult;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.utils.StringUtils;

import com.google.common.collect.ListMultimap;

import controllers.base.Home;
import controllers.product.CategoryProduct;
import dto.Category;

public class Application extends Controller {

	@Inject
	Home home;

	@Inject
	CategoryProduct category;

	public Promise<Result> index() {
		return home.home();
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
		F.Tuple<List<Category>, ListMultimap<Integer, Category>> res = category
				.getFirstSecondLevelCategories();
		return ok(views.xml.sitemap.render());
	}

	public Promise<Result> member() {
		String memberUrl = "";
		try {
			memberUrl = Play.application().configuration()
					.getString("member.defaultdomain");

		} catch (Exception e) {
			Logger.error(
					"From the configuration file to read the member jump address is empty!",
					e);
		}
		return Promise.pure(redirect(memberUrl + "/member/home"));
	}

	public Promise<Result> memberSuffix(String suffix) {
		String memberUrl = "";
		try {
			memberUrl = Play.application().configuration()
					.getString("member.defaultdomain");

		} catch (Exception e) {
			Logger.error(
					"From the configuration file to read the member jump address is empty!",
					e);
		}
		return Promise.pure(redirect(memberUrl + "/member/" + suffix));
	}

}
