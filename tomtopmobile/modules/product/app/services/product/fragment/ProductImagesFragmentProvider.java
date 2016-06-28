package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.image.IImageEnquiryService;
import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.ProductImages;
import dto.product.ProductImage;

public class ProductImagesFragmentProvider implements IProductFragmentProvider {

	@Inject
	IImageEnquiryService imageEnquiryService;

	public static final String NAME = "images";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<ProductImage> images = imageEnquiryService
				.getProductImages(context.getListingID());
		processingContext.put("productImages", images);
		return new ProductImages(images);
	}

}
