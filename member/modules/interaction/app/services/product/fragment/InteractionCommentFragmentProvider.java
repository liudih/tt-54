package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.interaciton.review.ProductReviewsService;
import services.member.IMemberEnquiryService;
import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

import com.google.common.collect.Lists;

import dto.ReviewsCountList;
import dto.interaction.Foverallrating;
import dto.interaction.InteractionComment;

public class InteractionCommentFragmentProvider implements
		IProductFragmentProvider {

	public static final String NAME = "comment";

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	ProductReviewsService productReviewsService;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		String listingid = context.getListingID();
		List<InteractionComment> clist = productReviewsService
				.getInteractionCommentsByListingId(listingid);

		List<Foverallrating> fovers = productReviewsService
				.getFoverallratingsGroup(listingid);
		
		List<ReviewsCountList>  rlist = Lists.newArrayList();
		if(clist.size()>0){
			rlist = productReviewsService.getReviewsCountList(clist);
		}
		
		Double averageScore = productReviewsService.getAverageScore(listingid);
		Integer reviewCount = productReviewsService.getReviewCount(listingid);
		
		int averageScoreStarWidth = 0;
		if (null != averageScore) {
			averageScoreStarWidth = (int) (Math.round((averageScore / 5) * 100));
		}
		
		
		String viewCommentUrl = controllers.interaction.review.routes.ProductReview.showAll(listingid,1).url();
		processingContext.put("viewCommentUrl", viewCommentUrl);
		processingContext.put("averageScore", averageScore);
		processingContext.put("reviewCount", reviewCount);
		processingContext.put("starsWidth", averageScoreStarWidth);
		processingContext.put("interactionComments", clist);

		return new valueobjects.interaction.Comment(listingid,rlist,
				averageScore, reviewCount, averageScoreStarWidth, fovers);
	}
}
