package services.cart.fragment.renderer;

import java.util.ArrayList;
import java.util.List;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.price.Price;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dto.product.ProductImage;

public class ProductFloatTopFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		Price price = (Price) context.getAttribute("sale-price");
		List<ProductImage> productImages = (List<ProductImage>) context
				.getAttribute("productImages");
		List<ProductImage> smallImages = new ArrayList<ProductImage>();
		String imgurl = "";
		if (null != productImages && productImages.size() > 0) {
			smallImages = Lists.newLinkedList(Collections2.filter(
					productImages, i -> i.getBsmallimage()));
			if (null != smallImages && smallImages.size() > 0) {
				imgurl = smallImages.get(0).getCimageurl();
			} else {
				imgurl = productImages.get(0).getCimageurl();
			}
		}
		return views.html.cart.product_top_float.render(context, price, imgurl);
	}
}
