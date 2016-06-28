package services.product.variables;

import java.util.List;
import java.util.Set;

import play.twirl.api.Html;
import valueobjects.product.ProductRenderContext;

import com.google.common.collect.Sets;

import dto.product.ProductEntityMap;
import extensions.product.IProductDescriptionVariableProvider;

public class ProductAttributeVariableProvider implements
		IProductDescriptionVariableProvider {

	@Override
	public Set<String> availableVariableNames() {
		return Sets.newHashSet("attributes");
	}

	/**
	 * 可以提供Html嵌入到Description中，或者普通POJO对象在FreeMarker中做提取
	 */
	@Override
	public Object get(String name, ProductRenderContext context) {
		StringBuffer sBuffer = new StringBuffer();
		@SuppressWarnings("unchecked")
		List<ProductEntityMap> entityMaps = (List<ProductEntityMap>) context
				.getAttribute("product_entity_map");

		if (null == entityMaps || entityMaps.size() == 0) {
			return Html.apply("");
		}

		sBuffer.append("<table class='Specifications_table' width='600' border='0' cellspacing='0' cellpadding='0'>");
		for (ProductEntityMap productEntityMap : entityMaps) {
			if (productEntityMap.isBshow() != null
					&& productEntityMap.isBshow()) {
				sBuffer.append("<tr><td>" + productEntityMap.getCkeyname()
						+ "</td><td>" + productEntityMap.getCvaluename()
						+ "</td></tr>");
			}
		}
		sBuffer.append("</table>");

		return Html.apply(sBuffer.toString());
	}

}
