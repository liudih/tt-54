package services.product;

import java.util.List;

import context.WebContext;
import dto.product.ProductMessage;

public interface IProductMessageService {

	public abstract List<ProductMessage> getProductMessageByListingIDs(
			List<String> listingIDs, WebContext context);

}