package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
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
		long starttime = System.currentTimeMillis();
		List<ProductImage> images = productImagerMapper
				.getProductImgsByListingId(context.getListingID());
		processingContext.put("productImages", images);
		Logger.debug("-->time-->ProductImagesFragmentProvider-->{}",
				System.currentTimeMillis() - starttime);
		return new ProductImages(images);
	}

}