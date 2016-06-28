package com.tomtop.website.migration.comment;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.website.migration.member.CustomerEntity;
import com.tomtop.website.migration.product.ProductEntity;

public interface CommentMapper {
	
	@Select("SELECT * FROM review_detail ORDER BY detail_id LIMIT #{0} OFFSET #{1}")
	public List<CommentEntity> getPageCommentEntity(int limit, int offset);
	
	@Select("SELECT * FROM review as r" 
			+ " inner join review_status as rs on r.status_id = rs.status_id"
			+ " inner join review_entity as re on r.entity_id = re.entity_id"
			+ " inner join review_store as r_store on r.review_id = r_store.review_id"
			+ " inner join review_detail as rd on r.review_id = rd.review_id"
			+ " inner join catalog_product_entity p on r.entity_pk_value =p.entity_id"
			+ " inner join customer_entity ce on rd.customer_id=ce.entity_id "
			+ " left join rating_option_vote rov on  r.review_id=rov.review_id "
			+ " ORDER BY r.review_id LIMIT #{0} OFFSET #{1}")
	public List<CommentEntity> getAllComment(int limit, int offset);
				

	@Select({"SELECT percent from rating_option_vote WHERE review_id = #{0} and rating_id = #{1}"}) 
	public Integer getRatingByReviewIdAndRatingId(Integer reviewId, Integer ratingId);
	

	
}
