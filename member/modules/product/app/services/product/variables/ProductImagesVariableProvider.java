package services.product.variables;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.product.ProductBaseTranslateService;
import valueobjects.product.ProductRenderContext;

import com.google.common.collect.Sets;

import dto.product.ProductImage;
import dto.product.ProductTranslate;
import extensions.product.IProductDescriptionVariableProvider;

public class ProductImagesVariableProvider implements
		IProductDescriptionVariableProvider {

	@Inject
	ProductBaseTranslateService translateService;

	@Inject
	FoundationService foundationService;

	@Override
	public Set<String> availableVariableNames() {
		return Sets.newHashSet("product_images");
	}

	@Override
	public Object get(String name, ProductRenderContext context) {
		StringBuffer sBuffer = new StringBuffer();

		@SuppressWarnings("unchecked")
		List<ProductImage> images = (List<ProductImage>) context
				.getAttribute("productImages");
		if (null == images || images.size() == 0) {
			return Html.apply("");
		}

		for (ProductImage productImage : images) {
			if (productImage.getBshowondetails() != null
					&& productImage.getBshowondetails()) {
				ProductTranslate parenTranslate = translateService
						.getTranslateByListingidAndLanguage(
								productImage.getClistingid(),
								foundationService.getLanguage());
				if (parenTranslate == null) {
					parenTranslate = translateService
							.getTranslateByListingidAndLanguage(
									productImage.getClistingid(),
									foundationService.getDefaultLanguage());
				}

				sBuffer.append("<br>"
						+ views.html.product.img_in_desc.render(productImage,
								parenTranslate.getCtitle()).body());
			}
		}
		return Html.apply(sBuffer.toString());
	}

}
