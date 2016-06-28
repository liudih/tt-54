package controllers.interaction;

import interaction.form.ProductAskQuestionForm;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.utils.DateFormatUtils;
import services.dodocool.base.FoundationService;
import services.interaction.product.post.IProductPostService;
import valueobjects.base.Page;
import valueobjects.dodocool.interaction.product.InteractionProductPost;
import authenticators.member.MemberLoginAuthenticator;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.interaction.ProductPost;

public class ProductFaq extends Controller {

	@Inject
	IProductPostService productPostService;

	@Inject
	FoundationService foundationService;

	@Inject
	DateFormatUtils dateFormatUtils;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result comment() {
		String email = foundationService.getLoginservice().getMemberID();
		Integer ilanguageid = foundationService.getLanguageId();
		Integer iwebsiteid = foundationService.getSiteID();
		Map<String, Object> resultMap = Maps.newHashMap();
		Form<ProductAskQuestionForm> form = Form.form(
				ProductAskQuestionForm.class).bindFromRequest();
		ProductAskQuestionForm formData = form.get();
		ProductPost productPost = new ProductPost();
		BeanUtils.copyProperties(formData, productPost);
		productPost.setDcreatedate(new Date());
		productPost.setCmemberemail(email);
		productPost.setItypeid(1); // 1是faq的类型
		productPost.setIlanguageid(ilanguageid);
		productPost.setIstate(1); // 1是表示审核通过
		productPost.setIwebsiteid(iwebsiteid);
		boolean result = productPostService.addProductPost(productPost);
		if (result) {
			resultMap.put("resultCode", "success");
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("resultCode", "failure");
		return ok(Json.toJson(resultMap));
	}

	@JsonGetter
	public Result getFaqs() {
		JsonNode json = request().body().asJson();
		String listingId = json.get("listingId").asText();
		Integer page = json.get("page").asInt();
		Integer pageSize = json.get("pageSize").asInt();
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		String sku = json.get("sku").asText();
		Integer itypeid = 1; // 1表示faq
		Integer state = 1; // 1表示审核通过
		Page<Map<String, List<dto.interaction.ProductPost>>> faqPage = productPostService
				.getProductPostList(listingId, webContext, itypeid, state,
						page, pageSize);
		Boolean isLogined = foundationService.isLogined();
		return ok(views.html.interaction.product.fragment.product_faq_tr
				.render(new InteractionProductPost(faqPage, listingId,
						isLogined, sku)));
	}

	@JsonGetter
	@Authenticated(MemberLoginAuthenticator.class)
	public Result reply() throws ParseException {
		String email = foundationService.getLoginservice().getMemberID();
		Integer iwebsiteid = foundationService.getSiteID();
		Integer ilanguage = foundationService.getLanguageId();
		JsonNode json = request().body().asJson();
		String listingId = json.get("listingId").asText();
		String sku = json.get("sku").asText();
		String title = json.get("title").asText();
		String question = json.get("question").asText();
		String answer = json.get("answer").asText();
		String createUserEmail = json.get("email").asText();
		Long dateTime = json.get("date").asLong();
		Date date = new Date(dateTime);
		ProductPost faq = new ProductPost();
		faq.setDcreatedate(date);
		faq.setDrecoverydate(new Date());
		faq.setCanswer(answer);
		faq.setClistingid(listingId);
		faq.setCsku(sku);
		faq.setCmemberemail(createUserEmail);
		faq.setCrecoveryuser(email);
		faq.setCtitle(title);
		faq.setItypeid(1);
		faq.setIstate(1);
		faq.setIwebsiteid(iwebsiteid);
		faq.setCquestion(question);
		faq.setIlanguageid(ilanguage);
		Boolean result = productPostService.addProductPost(faq);
		if (result) {
			return ok("success");
		}
		return ok("failure");
	}
}
