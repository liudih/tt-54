package controllers.interaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

import play.mvc.Controller;
import play.mvc.Result;
import services.interaction.review.IProductReviewsService;
import services.member.IMemberEnquiryService;
import valueobjects.base.Page;
import valueobjects.dodocool.interaction.product.InteractionComment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import dto.ReviewsCountList;

public class ProductReviews extends Controller {

	@Inject
	IProductReviewsService productReviewsService;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@JsonGetter
	public Result getReviews() {
		JsonNode json = request().body().asJson();
		String listingId = json.get("listingId").asText();
		Integer page = json.get("page").asInt();
		Integer pageSize = json.get("pageSize").asInt();
		Page<ReviewsCountList> reviewsPage = productReviewsService.getPages(
				listingId, page, pageSize);
		List<ReviewsCountList> reviewsCountLists = reviewsPage.getList();
		Set<String> emails = Sets.newHashSet(Lists.newArrayList(Lists
				.transform(reviewsCountLists, i -> i.getEmail())));
		Map<String, String> emailAndNameMap = Maps.asMap(emails,
				i -> memberEnquiryService.getUserNameByMemberEmail(i));
		InteractionComment reviews = new InteractionComment(listingId,
				reviewsPage, emailAndNameMap);
		return ok(views.html.interaction.product.fragment.product_reviews_tr
				.render(reviews));
	}
}
