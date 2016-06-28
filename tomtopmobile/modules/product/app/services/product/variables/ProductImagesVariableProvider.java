package services.product.variables;

import java.util.List;
import java.util.Set;

import play.twirl.api.Html;
import valueobjects.product.ProductRenderContext;

import com.google.common.collect.Sets;

import dto.product.ProductImage;
import extensions.product.IProductDescriptionVariableProvider;

public class ProductImagesVariableProvider implements
		IProductDescriptionVariableProvider {

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
		//images.get(0).get
		sBuffer.append("<div style='text-align:center'> ");
		for (ProductImage productImage : images) {
			sBuffer.append("<br>"
					+ views.html.product.img_in_desc.render(productImage)
							.body());
		}
		sBuffer.append("</div>");
		return Html.apply(sBuffer.toString());
	}

}
