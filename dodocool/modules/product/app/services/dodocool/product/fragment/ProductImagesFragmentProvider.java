package services.dodocool.product.fragment;

import java.util.List;
import java.util.Map;

import services.dodocool.product.IProductFragmentProvider;
import services.image.IImageEnquiryService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.ProductImages;

import com.google.inject.Inject;

import dto.product.ProductImage;

public class ProductImagesFragmentProvider implements IProductFragmentProvider {

	@Inject
	IImageEnquiryService imageEnquiry;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<ProductImage> images = imageEnquiry.getProductImages(context
				.getListingID());
		return new ProductImages(images);
	}

}
