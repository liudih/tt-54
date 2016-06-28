package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductImageMapper;
import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.ProductImages;
import dto.product.ProductImage;

public class ProductImagesFragmentProvider implements IProductFragmentProvider {

	public static final String NAME = "images";

	@Inject
	ProductImageMapper productImagerMapper;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<ProductImage> images = productImagerMapper
				.getProductImgsByListingId(context.getListingID());
		processingContext.put("productImages", images);
		return new ProductImages(images);
	}

}