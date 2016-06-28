package services.dodocool.product.fragment;

import java.util.Map;

import play.Logger;
import services.dodocool.product.IProductFragmentProvider;
import services.product.IProductEnquiryService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductBase;
import valueobjects.product.ProductContext;

import com.google.inject.Inject;

public class ProductBaseFragmentProvider implements IProductFragmentProvider {

	@Inject
	IProductEnquiryService productEnquiryService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		dto.product.ProductBase productbase = productEnquiryService
				.getProductByListingIdAndLanguageWithdoutDesc(
						context.getListingID(), context.getLang());
		processingContext.put("sku", context.getSku());
		Logger.debug("listingId " + context.getListingID() + " lang "
				+ context.getLang() + "==pr==" + productbase);
		String desccript = "";
		if (productbase != null) {
			processingContext.put("product-title", productbase.getCtitle());
			StringBuffer sb = new StringBuffer();
			int start = 0;
			int len = 20000;
			while (true) {
				String desc = productEnquiryService
						.getProductDescByListingIdAndLanguagePart(
								context.getListingID(), context.getLang(),
								start, len);
				start += len;
				if (desc == null)
					break;
				sb.append(desc);
				Logger.debug("len+ == {} ", sb.toString().length());
			}
			desccript = sb.toString();
			productbase.setCdescription(desccript);
		}
		desccript = desccript.replace("${attributes}", "");
		desccript = desccript.replace("${product_images}", "");
		processingContext.put("product-overview", desccript);
		return new ProductBase(productbase, null);
	}
}
