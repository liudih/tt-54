package controllers.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;
import services.interaction.product.post.ProductPostService;
import session.ISessionService;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import context.ContextUtils;
import context.WebContext;
import controllers.InterceptActon;
import dto.interaction.ProductPost;
import entity.manager.AdminUser;
import events.member.ReplyMemberFaqEvent;
import forms.ProductPostForm;
import forms.ProductPostSearchForm;

@With(InterceptActon.class)
public class ProductFAQ extends Controller {
	@Inject
	ProductPostService fAQService;
	@Inject
	ISessionService sessionService;
	@Inject
	EventBus eventBus;

	public Result index(int page, int pageSize) {
		HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
		statusMap.put(0, "待审核");
		statusMap.put(1, "审核通过");
		statusMap.put(2, "审核不通过");

		return ok(views.html.manager.product.faq.index.render(statusMap));
	}

	public Result search() {
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		Form<ProductPostSearchForm> productPostSearchForm = Form.form(
				ProductPostSearchForm.class).bindFromRequest();
		ProductPostSearchForm postSearchForm = productPostSearchForm.get();
		Integer itypeid = postSearchForm.getTypeId();
		Integer state = postSearchForm.getStatus();
		Integer page = postSearchForm.getPageNum();
		Integer pageSize = postSearchForm.getPageSize();
		Integer languageId = postSearchForm.getLanguageId();
		Integer websiteId = postSearchForm.getSiteId();

		List<ProductPost> productPostList = fAQService
				.getProductPostListBySearch(websiteId, languageId, itypeid,
						state, page, pageSize);
		int total = fAQService.getTotalBySearch(websiteId, languageId, itypeid,
				state);

		Integer pageTotal = total / postSearchForm.getPageSize()
				+ ((total % postSearchForm.getPageSize() > 0) ? 1 : 0);

		return ok(views.html.manager.product.faq.post_table_list.render(
				productPostList, total, pageTotal, page));
	}

	public Result updatePostAnswer() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		Form<ProductPostForm> ProductPostForm = Form
				.form(ProductPostForm.class).bindFromRequest();
		ProductPostForm productPostForm2 = ProductPostForm.get();
		ProductPost productPost = new ProductPost();
		BeanUtils.copyProperties(productPostForm2, productPost);
		productPost.setCrecoveryuser(user.getCusername());
		productPost.setDrecoverydate(new Date());
		productPost.setBreply(true);
		boolean updateProductPost = fAQService.updateProductPost(productPost);
		Map<String, Boolean> resultMap = Maps.newHashMap();
		resultMap.put("result", updateProductPost);
		if (updateProductPost) {
			ProductPost productPostByIid = fAQService
					.getProductPostByIid(productPost.getIid());
			ReplyMemberFaqEvent replyMemberFaqEvent = new ReplyMemberFaqEvent(
					productPostByIid.getCmemberemail(),
					productPostByIid.getCquestion(),
					productPostByIid.getCanswer(),
					productPostByIid.getIwebsiteid(),
					productPostByIid.getIlanguageid());
			eventBus.post(replyMemberFaqEvent);
		}

		return ok(Json.toJson(resultMap));
	}
}
