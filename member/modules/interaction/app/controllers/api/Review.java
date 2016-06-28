package controllers.api;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.interaciton.review.ProductReviewsUpdateService;

import com.fasterxml.jackson.databind.JsonNode;
import com.website.dto.comment.Comment;

import dto.interaction.InteractionComment;
import dto.product.ProductBase;

public class Review extends Controller {

	@Inject
	ProductReviewsUpdateService productReviewsUpdateService;

	/**
	 * 单品发布，多属性发布，品类关联
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result push() {
		try {
			// String user = request().getHeader("user-token");
			Logger.debug("----" + request().body().asJson());
			JsonNode node = request().body().asJson();
			if (node == null) {
				return badRequest("Expecting Json data");
			}
			this.saveCommentBase(node);
			return ok("successfully");
		} catch (Exception p) {
			Logger.error("exception error", p);
			return internalServerError(p.getMessage());
		}
	}

	public void saveCommentBase(JsonNode node) {
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				Comment comment = Json.fromJson(nodeiterator.next(),
						Comment.class);
				if (productReviewsUpdateService.checkCommentExists(comment)) {
					continue;
				}
				List<InteractionComment> interactionComments = _initComment(comment);
				// 找不到listingId就不导该评论
				if (null == interactionComments) {
					continue;
				}
				for (InteractionComment interactionComment : interactionComments) {
					productReviewsUpdateService.saveComment(interactionComment);
				}
			}
		} else {
			Comment comment = Json.fromJson(node, Comment.class);
			Logger.debug("=======comment: " + Json.toJson(comment));
			if (productReviewsUpdateService.checkCommentExists(comment)) {
				return;
			}
			List<InteractionComment> interactionComments = _initComment(comment);
			if (null == interactionComments) {
				return;
			}
			for (InteractionComment interactionComment : interactionComments) {
				productReviewsUpdateService.saveComment(interactionComment);
			}

		}
	}

	private List<InteractionComment> _initComment(Comment comment) {

		// getListingId
		List<ProductBase> productBases = productReviewsUpdateService
				.getListingId(comment.getSku(), comment.getWebsiteId());
		if (null == productBases) {
			Logger.debug("-----no listing id for this sku: " + comment.getSku());
			return null;
		}
		List<InteractionComment> comentlist = Lists
				.transform(
						productBases,
						productBase -> {
							InteractionComment interactionComment = new InteractionComment();
							interactionComment.setClistingid(productBase
									.getClistingid());
							interactionComment.setCcomment(comment.getComment());
							interactionComment.setClistingid(comment
									.getListingId());
							interactionComment.setCmemberemail(comment
									.getMemberEmail());
							interactionComment.setCsku(comment.getSku());
							interactionComment.setDauditdate(comment
									.getAuditDate());
							interactionComment.setDcreatedate(comment
									.getCreatedDate());
							interactionComment.setFoverallrating(comment
									.getFoverallrating());
							interactionComment.setIprice(comment.getPrice());
							interactionComment.setIquality(comment.getQuality());
							interactionComment.setIshipping(comment
									.getShipping());
							interactionComment.setIstate(comment.getState());
							interactionComment.setIusefulness(comment
									.getUsefulness());
							return interactionComment;

						});
		return comentlist;
	}

}