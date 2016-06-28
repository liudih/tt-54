package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.HtmlTidy;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class ProductPolicyFragmentRender implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {

		String cpaymentexplain = HtmlTidy.tidy((String) context
				.getAttribute("paymentexplain"));
		String creturnexplain = HtmlTidy.tidy((String) context
				.getAttribute("returnexplain"));
		String cwarrantyexplain = HtmlTidy.tidy((String) context
				.getAttribute("warrantyexplain"));

		return views.html.product.fragment.product_policy.render(
				cpaymentexplain, creturnexplain, cwarrantyexplain);
	}

}
