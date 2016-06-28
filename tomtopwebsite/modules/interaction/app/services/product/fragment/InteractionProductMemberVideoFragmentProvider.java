package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import dto.interaction.InteractionProductMemberVideo;
import mapper.interaction.InteractionProductMemberVideoMapper;
import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public class InteractionProductMemberVideoFragmentProvider implements
		IProductFragmentProvider {

	public static final String NAME = "member-video";

	@Inject
	InteractionProductMemberVideoMapper memberVideoMapper;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		long starttime = System.currentTimeMillis();
		List<InteractionProductMemberVideo> membervideos = memberVideoMapper
				.getMemberVideosBylistId(context.getListingID());
		Logger.debug(
				"--->time-->InteractionProductMemberVideoFragmentProvider--> {} ",
				System.currentTimeMillis() - starttime);
		return new valueobjects.interaction.InteractionProductMemberVideo(
				membervideos, null, context.getListingID());
	}

}
