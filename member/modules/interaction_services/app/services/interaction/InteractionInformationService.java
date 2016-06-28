package services.interaction;

import mapper.interaction.InteractionCommentMapper;
import mapper.member.MemberBaseMapper;

import com.google.inject.Inject;

public class InteractionInformationService {
	/**
	 * 提供与会员模块相关的简单信息
	 */

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Inject
	InteractionCommentMapper interactionCommentMapper;

	/**
	 * 获取用户名称
	 *
	 * @param userEmail
	 *            用户 邮箱
	 * @return 用户名称
	 */
	public String getName(String userEmail) {
		return memberBaseMapper.getUserName(userEmail);
	}

	/**
	 * 获取商品的用户评论数
	 *
	 * @param listingId
	 *            商品listing ID
	 * @param websiteId
	 *            站点 ID
	 * @return 评论数量
	 */
	public Integer getProductCommentCount(String listingId) {
		return interactionCommentMapper.getCountByListingId(listingId);
	}

	/**
	 * 获取商品的评分
	 *
	 * @param listingId
	 *            商品listing ID
	 * @param websiteId
	 *            站点 ID
	 * @return 评分
	 */
	public Double getProductScore(String listingId) {
		return interactionCommentMapper.getScoreByListingId(listingId);
	}

}
