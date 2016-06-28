package services.product;

import java.util.Set;

import javax.inject.Inject;

import valueobjects.product.ProductCommentContext;
import extensions.product.IProductCommentProvider;

public class ProductCommentEnquiry {

	@Inject
	Set<IProductCommentProvider> productCommentProviders;

	/**
	 * 复制评论
	 * 
	 * @param context
	 * @return true 是否成功
	 */
	public boolean copyComment(ProductCommentContext context) {
		if (productCommentProviders != null
				&& productCommentProviders.size() > 0) {
			return ((IProductCommentProvider) productCommentProviders.toArray()[0])
					.copyComment(context);
		}
		return false;
	}

}
