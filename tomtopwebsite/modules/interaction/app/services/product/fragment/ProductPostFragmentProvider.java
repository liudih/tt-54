package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import dto.interaction.ProductPostType;
import mapper.interaction.ProductPostMapper;
import mapper.member.MemberBaseMapper;
import services.base.FoundationService;
import services.interaction.product.post.ProductPostService;
import services.product.IProductFragmentProvider;
import valueobjects.interaction.ProductPostList;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public class ProductPostFragmentProvider implements IProductFragmentProvider {

	public static final String NAME = "post";

	@Inject
	ProductPostService postService;

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Inject
	FoundationService foundationService;

	@Override
	public String getName() {
		return NAME;
	}

	final int limit = 15;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		long starttime = System.currentTimeMillis();
		String listingid = context.getListingID();
		Integer typeId = 1;
		List<dto.ProductPost> list = postService.getByListingId(listingid,
				limit);
		int total = postService.getTotalRecord(listingid,
				ProductPostMapper.CHECKED, foundationService.getLanguage(),
				foundationService.getSiteID(), typeId);
		Logger.debug("--->time-->ProductPostFragmentProvider--> {} ",
				System.currentTimeMillis() - starttime);
		return new ProductPostList(total, list);
	}

}
