package services.interaction.review;

import context.WebContext;
import valueobjects.base.Page;
import valueobjects.interaction.ReviewsInMemberCenter;

public interface IMemberReviewsServiece {

	Integer getTotalReviewsCountByMemberEmailAndSiteId(String email,
			WebContext context);

	Integer getPendingReviewsCountByMemberEmailAndSiteId(String email,
			WebContext context);

	Integer getApprovedReviewsCountByMemberEmailAndSiteId(String email,
			WebContext context);

	Integer getFailedReviewsCountByMemberEmailAndSiteId(String email,
			WebContext context);

	Page<ReviewsInMemberCenter> getMyReviewsPageByEmail(String email,
			Integer total, Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, WebContext context);
}
