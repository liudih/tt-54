package services.product.fragment;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import mapper.interaction.InteractionCommentMapper;
import mapper.interaction.InteractionProductMemberPhotosMapper;

import org.elasticsearch.common.collect.Sets;

import play.Logger;
import services.member.IMemberEnquiryService;
import services.product.IProductFragmentProvider;
import valueobjects.interaction.MemberPhoto;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.interaction.InteractionProductMemberPhotos;
import dto.member.MemberBase;

public class InteractionProductMemberPhotoProvider implements
		IProductFragmentProvider {
	public static final String NAME = "member-photos";

	@Inject
	InteractionProductMemberPhotosMapper mempmapper;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	InteractionCommentMapper interComment;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		long starttime = System.currentTimeMillis();
		List<InteractionProductMemberPhotos> list = mempmapper
				.getProductMemberPhotoByListingId(context.getListingID());

		if (list == null || list.size() <= 0) {
			return null;
		}

		Set<String> emailSets = Sets.newHashSet();

		final Map<String, List<InteractionProductMemberPhotos>> photosMap = Maps
				.newHashMap();

		for (InteractionProductMemberPhotos photo : list) {
			String email = photo.getCmemberemail();
			emailSets.add(email);
			if (photosMap.containsKey(email)) {
				photosMap.get(email).add(photo);
			} else {
				List<InteractionProductMemberPhotos> photos = Lists
						.newArrayList();
				photos.add(photo);
				photosMap.put(email, photos);
			}
		}

		List<MemberBase> members = memberEnquiryService
				.getUserNamesByMemberEmail(Lists.newArrayList(emailSets));

		List<MemberPhoto> result = Lists.transform(members,
				new Function<MemberBase, MemberPhoto>() {

					@Override
					public MemberPhoto apply(MemberBase m) {
						String email = m.getCemail();
						String username = m.getCaccount() == null ? email : m
								.getCaccount();
						List<InteractionProductMemberPhotos> photos = photosMap
								.get(email);
						return new MemberPhoto(photos, username);
					}
				});
		Logger.debug("--->time-->InteractionProductMemberPhotoProvider--> {} ",
				System.currentTimeMillis() - starttime);
		return new valueobjects.interaction.InteractionProductMemberPhotos(
				result);
	}
}
