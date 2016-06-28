package services.interaction.prodcut.fragment;

import java.util.List;
import java.util.Map;
import java.util.Set;

import services.dodocool.member.MemberNameService;
import services.dodocool.product.IProductFragmentProvider;
import services.interaction.review.IProductReviewsService;
import services.member.IMemberEnquiryService;
import valueobjects.base.Page;
import valueobjects.dodocool.interaction.product.InteractionComment;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import dto.ReviewsCountList;

public class ProductCustomerReviewFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	IProductReviewsService productReviewsService;

	@Inject
	MemberNameService memberNameService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		String listingId = context.getListingID();
		int page = 1;
		int pageSize = 10;
		Page<ReviewsCountList> reviewsPage = productReviewsService.getPages(
				listingId, page, pageSize);
		List<ReviewsCountList> reviewsCountLists = reviewsPage.getList();
		Set<String> emails = Sets.newHashSet(Lists.newArrayList(Lists
				.transform(reviewsCountLists, i -> i.getEmail())));
		Map<String, String> emailAndNameMap = Maps.asMap(emails,
				i -> memberNameService.getMemberAccountNameByEmail(i));
		return new InteractionComment(listingId, reviewsPage, emailAndNameMap);
	}
}
