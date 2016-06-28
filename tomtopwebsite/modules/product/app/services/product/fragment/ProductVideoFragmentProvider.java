package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import dao.product.IProductVideoEnquiryDao;
import dto.product.ProductVideo;

public class ProductVideoFragmentProvider implements IProductFragmentProvider {

	public static final String NAME = "video";

	@Inject
	IProductVideoEnquiryDao productVideoEnquityDao;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		long starttime = System.currentTimeMillis();
		List<ProductVideo> videos = this.productVideoEnquityDao
				.getVideosBylistId(context.getListingID());
		Logger.debug("-->time-->ProductVideoFragmentProvider-->{}",
				System.currentTimeMillis() - starttime);
		return new valueobjects.product.ProductVideo(videos);
	}

}
