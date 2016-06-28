package extensions.product;

import valueobjects.product.ProductCommentContext;

public interface IProductCommentProvider {
	/**
	 * 
	 * copy评论
	 * 
	 * @return
	 */
	boolean copyComment(ProductCommentContext context);
}
