package services.product;

import java.util.Map;

import valueobjects.product.IProductFragment;
import valueobjects.product.ProductComposite;
import valueobjects.product.ProductContext;

/**
 * Provide single fragment for product composite used for product page rendering
 */
public interface IProductFragmentProvider {

	@Deprecated
	String getName();

	/**
	 *
	 * @param context
	 *            产品查找条件
	 * @param processingContext
	 *            在准备过程中，有特别数据要返回的，可以用此对象
	 * @return
	 * @see ProductComposite#getAttributes()
	 */
	IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext);

}
