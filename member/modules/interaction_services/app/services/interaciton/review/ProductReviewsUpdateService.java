package services.interaciton.review;

import java.util.List;

import javax.inject.Inject;

import com.website.dto.comment.Comment;

import mapper.interaction.InteractionCommentMapper;
import mapper.product.ProductBaseMapper;
import dto.interaction.InteractionComment;
import dto.product.ProductBase;

public class ProductReviewsUpdateService {
	
	public final static  String POSITIVE = "POSITIVE"; //好评
	public final static String NEUTRAL = "NEUTRAL"; //中评
	public final static String NEGATIVE = "NEGATIVE"; //差评
	

	@Inject
	InteractionCommentMapper commentMapper;
	@Inject
	ProductBaseMapper productBaseMapper;

	/**
	 * 新增评论
	 *
	 * @param Comment
	 * 
	 * @return int
	 */
	public Integer saveComment(InteractionComment comment) {
		try {
			return commentMapper.insertComment(comment);
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public List<ProductBase> getListingId(String sku, Integer websiteId) {
		return productBaseMapper
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
	}
	
	public ProductBase getListingIdByParams(String sku, Integer websiteId) {
		return (ProductBase) productBaseMapper
				.getProductBaseByParams(sku, websiteId);
	}

	public Boolean checkCommentExists(Comment comment) {
		InteractionComment interactionComment = commentMapper
				.getCommentByParams(comment.getSku(), comment.getMemberEmail(),
						comment.getComment());

		return null == interactionComment ? false : true;
	}

}
