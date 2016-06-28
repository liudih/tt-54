package controllers;

import interceptors.CacheResult;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;

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
		return Promise.pure(redirect("/member/home"));
	}

	
	public Result robots() {
		String str = getRobotsTxt();
		return ok(str).as("text/plain");
	}


	@CacheResult
	private String getRobotsTxt() {
		String str = "";
		try {
			str = IOUtils.toString(Application.class.getClassLoader().getResourceAsStream("robots.txt"));
		} catch (IOException e) {
			Logger.info("read robots.txt file error");
		}
		return str;
	}

	public Result sitemap() {
		//F.Tuple<List<Category>, ListMultimap<Integer, Category>> res = category
		//		.getFirstSecondLevelCategories();
		return ok(views.xml.sitemap.render());
	}
}
