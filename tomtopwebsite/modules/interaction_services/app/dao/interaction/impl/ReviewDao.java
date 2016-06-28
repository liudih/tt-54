package dao.interaction.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.interaction.InteractionCommentMapper;
import dao.interaction.IReviewDao;
import dto.interaction.InteractionComment;

public class ReviewDao implements IReviewDao {

	@Inject
	InteractionCommentMapper interactionCommentMapper;

	@Override
	public List<InteractionComment> getReviewsByPageAndListingIdAndStatus(
			Integer page, Integer pageSize, String sku, Integer status,
			String email, Date startDate, Date endDate, String content,
			Integer siteId) {
		return interactionCommentMapper.getReviewsByPageAndListingIdAndStatus(
				page, pageSize, sku, status, email, startDate, endDate,
				content, siteId);
	}

	@Override
	public Integer getReviewsCount(String sku, Integer status, String email,
			Date startDate, Date endDate, String content, Integer siteId) {
		return interactionCommentMapper.getReviewsCount(sku, status, email,
				startDate, endDate, content, siteId);
	}

	@Override
	public InteractionComment getReviewById(Integer id) {
		return interactionCommentMapper.selectByPrimaryKey(id);
	}

	@Override
	public Boolean updateReviewStatus(InteractionComment review) {
		int result = interactionCommentMapper
				.updateByPrimaryKeySelective(review);
		return result > 0 ? true : false;
	}

	@Override
	public List<InteractionComment> getInteractionCommentsByListingId(
			String listingid) {
		return interactionCommentMapper
				.getInteractionCommentsByListingId(listingid);
	}

	@Override
	public int batchVerify(List<Integer> idList, Integer status) {
		return interactionCommentMapper.batchVerify(idList, status);
	}
}
