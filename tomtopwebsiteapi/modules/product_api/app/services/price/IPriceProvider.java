package services.price;

import java.util.List;

import valueobjects.price.Price;
import valueobjects.price.PriceCalculationContext;
import valueobjects.product.spec.IProductSpec;

public interface IPriceProvider {

	/**
	 * 返回产品的基础价格。所谓产品可以是单一个Listing，也可以是捆绑的几个Listing。具体通过检查items的实现类来判断
	 * 
	 * @param items
	 * @param ctx
	 * @return
	 */
	List<Price> getPrice(List<IProductSpec> items, PriceCalculationContext ctx);

	/**
	 * 检查是否可以使用这个价格提供方，用于过滤整个<code>List&lt;IProductSpec&gt;</code>
	 * 
	 * @param item
	 * @return
	 */
	boolean match(IProductSpec item);
}
