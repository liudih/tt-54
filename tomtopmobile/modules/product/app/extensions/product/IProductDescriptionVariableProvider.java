package extensions.product;

import java.util.Set;

import valueobjects.product.ProductRenderContext;

/**
 * 提供ProductDescription所需要的参数
 * 
 * @author kmtong
 *
 */
public interface IProductDescriptionVariableProvider {

	Set<String> availableVariableNames();

	Object get(String name, ProductRenderContext context);

}
