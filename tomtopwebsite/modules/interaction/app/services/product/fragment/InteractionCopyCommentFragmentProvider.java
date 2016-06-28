package services.product.fragment;

import java.util.List;

import javax.inject.Inject;

import dto.interaction.InteractionComment;
import dto.interaction.InteractionProductMemberPhotos;
import dto.interaction.InteractionProductMemberVideo;
import mapper.interaction.InteractionCommentMapper;
import mapper.interaction.InteractionProductMemberPhotosMapper;
import mapper.interaction.InteractionProductMemberVideoMapper;
import valueobjects.product.ProductCommentContext;
import extensions.product.IProductCommentProvider;

public class InteractionCopyCommentFragmentProvider implements
		IProductCommentProvider {

	@Inject
	InteractionCommentMapper interactionCommentMapper;

	@Inject
	InteractionProductMemberPhotosMapper interactionProductMemberPhotosMapper;

	@Inject
	InteractionProductMemberVideoMapper videoMapper;

	@Override
	public boolean copyComment(ProductCommentContext context) {

		List<InteractionComment> comments = interactionCommentMapper
				.getInteractionCommentsByListingId(context
						.getCparentlistingid());
		List<InteractionProductMemberVideo> video = videoMapper
				.getMemberVideos(context.getCparentlistingid());
		List<InteractionProductMemberPhotos> photo = interactionProductMemberPhotosMapper
				.getProductMemberPhotoByListingId(context.getCparentlistingid());
		/**
		 * 1.根据旧的产品listingid找到评论 2.复制评论 3.插入新评论
		 */
		if (comments.size() != 0) {
			for (InteractionComment ic : comments) {
				ic.setClistingid(context.getCsublistingid());
				ic.setCsku(context.getCsubcku());
				interactionCommentMapper.insertSelective(ic);
			}
		}
		/**
		 * 1.根据旧的产品listingid找到客户上传的图片 2.复制图片 3.插入新图片
		 */
		if (photo.size() != 0) {
			for (InteractionProductMemberPhotos memberPhoto : photo) {
				memberPhoto.setClistingid(context.getCsublistingid());
				interactionProductMemberPhotosMapper.insert(memberPhoto);
			}
		}
		/**
		 * 1.根据旧的产品listingid找到客户上传的视频 2.复制视频 3.插入新视频
		 */
		if (video.size() != 0) {
			for (InteractionProductMemberVideo memberVideo : video) {
				memberVideo.setClistingid(context.getCsublistingid());
				videoMapper.insert(memberVideo);
			}
		}
		return true;
	}

}
