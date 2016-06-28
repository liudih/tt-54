package controllers.interaction;

import java.util.List;

import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.interaction.product.post.IProductPostService;

import com.google.inject.Inject;

import context.ContextUtils;
import dto.interaction.ProductPost;

public class Faq extends Controller {
	@Inject
	IProductPostService productPostService;

	public Result home() {
		Integer typeId = 1;
		Integer state = 0;
		Integer page = 1;
		Integer pageSize = 10;
		List<ProductPost> productPostList = productPostService
				.getProductPostList(
						ContextUtils.getWebContext(Context.current()), typeId,
						state, page, pageSize);
		return ok(views.html.interaction.support.faq.render(productPostList));
	}

	public Result search(String key) {
		Integer typeId = 1;
		Integer state = 0;
		Integer page = 1;
		Integer pageSize = 10;
		List<ProductPost> productPostList = productPostService
				.getProductPostListBySearch(
						ContextUtils.getWebContext(Context.current()), key,
						typeId, state, page, pageSize);
		return ok(views.html.interaction.support.faq_list
				.render(productPostList));
	}
}
