package com.tomtop.website.upload;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtop.website.migration.comment.CommentEntity;
import com.website.dto.comment.Comment;

public class CommentConvertor {

	public JsonNode getCommnet(JsonNode jnode) {
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			CommentEntity commentEntity = jsonMapper.treeToValue(jnode,
					CommentEntity.class);
			Comment comment = new Comment();
			comment.setMemberEmail(commentEntity.getEmail());
			comment.setSku(commentEntity.getSku());
			comment.setComment(commentEntity.getDetail());
			if (commentEntity.getStatus_id() == 1)
				comment.setState(commentEntity.getStatus_id());
			else if (commentEntity.getStatus_id() == 2)
				comment.setState(0);
			else if (commentEntity.getStatus_id() == 3)
				comment.setState(2);
			comment.setCreatedDate(commentEntity.getCreated_at());
			comment.setStoreId(commentEntity.getStore_id());
			comment.setWebsiteId(1);
			// has no value
			comment.setAuditDate(null);
			comment.setListingId(null);
			Integer val = commentEntity.getValue();
			if (commentEntity.getValue() == null) {
				val = 5;
			}
			comment.setFoverallrating(Double.parseDouble(val.toString()));
			comment.setUsefulness(val);
			comment.setPrice(val);
			comment.setQuality(val);
			comment.setShipping(val);
			comment.setWebsiteId(1);
			return jsonMapper.convertValue(comment, JsonNode.class);

		} catch (Exception e) {
			e.printStackTrace();

			return null;

		}

	}
}
