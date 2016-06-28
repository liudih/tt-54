package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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
		
		List<ProductVideo> videos = this.productVideoEnquityDao
				.getVideosBylistId(context.getListingID());
		
		return new valueobjects.product.ProductVideo(videos);
	}

}
