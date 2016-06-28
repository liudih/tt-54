package com.tomtop.interaction.mappers;

import org.apache.ibatis.annotations.Select;

public interface InteractionCommentMapper {

	@Select("select max(iid+1) from t_interaction_comment")
	Integer getReviewMaxId();
	
}