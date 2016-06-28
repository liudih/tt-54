package services.interaction.review;

import java.util.Date;
import java.util.List;
import java.util.Map;

import valueobjects.base.Page;
import valueobjects.interaction.ReviewCountAndScore;
import context.WebContext;
import dto.ReviewsCountList;
import dto.interaction.Foverallrating;
import dto.interaction.InteractionComment;
import dto.interaction.InteractionProductMemberVideo;

public interface IProductReviewsService {

	Map<String, ReviewCountAndScore> getAverageScores(List<String> listingIds,
			ReviewCountAndScore defaultIfNotFound);

	Page<ReviewsCountList> getPages(String listingId, Integer page,
			Integer pageSize);

	List<ReviewsCountList> getReviewsCountList(List<InteractionComment> reviews);

	Integer getReviewCount(String listingId);

	Double getAverageScore(String listingId);

	Integer doAddProductReview(InteractionComment review);

	List<InteractionComment> getInteractionCommentsByListingId(String listingid);

	Boolean updateReview(InteractionComment review);

	List<InteractionComment> getReviews(Integer page, Integer pageSize,
			String sku, Integer status, String email, Date startDate,
			Date endDate, String content, WebContext context);

	InteractionComment getReviewById(Integer id);

	Integer getReviewsCount(String sku, Integer status, String email,
			Date startDate, Date endDate, String content, WebContext context);

	List<Foverallrating> getFoverallratingsGroup(String listingId);

	boolean checkUploadPhoto(String email, String listingId);

	boolean sendEmailReviewSubmit(String toemail, WebContext context);

	String checkReview(String email, String listingId);

	public boolean addVideoReview(InteractionProductMemberVideo video);

	public Integer getMaxId();

}
