package dao.interaction;

import java.util.Date;
import java.util.List;

import dto.interaction.InteractionComment;

public interface IReviewDao {

	List<InteractionComment> getReviewsByPageAndListingIdAndStatus(
			Integer page, Integer pageSize, String sku, Integer status,
			String email, Date startDate, Date endDate, String content, Integer siteId);

	Integer getReviewsCount(String sku, Integer status, String email,
			Date startDate, Date endDate, String content, Integer siteId);

	InteractionComment getReviewById(Integer id);

	Boolean updateReviewStatus(InteractionComment review);

	List<InteractionComment> getInteractionCommentsByListingId(String listingid);

	int batchVerify(List<Integer> idList, Integer status);

}
