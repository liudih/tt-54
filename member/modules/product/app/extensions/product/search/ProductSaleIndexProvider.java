package extensions.product.search;

import java.io.Serializable;

import javax.inject.Inject;

import mapper.product.ProductCategoryRankMapper;
import play.libs.Json;
import valueobjects.search.ProductIndexingContext;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dto.product.ProductCategoryRank;
import extensions.search.ISearchIndexProvider;

public class ProductSaleIndexProvider implements ISearchIndexProvider,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	ProductCategoryRankMapper productrank;

	@Override
	public String partName() {
		return "sales";
	}

	@Override
	public ObjectNode indexPart(ProductIndexingContext context) {
		ObjectNode obj = Json.newObject();
		ProductCategoryRank prank = productrank.selectSales(
				context.getListingId(), context.getSiteId());
		int sale = 0;
		if (prank != null) {
			sale = prank.getIsales();
		}
		obj.put("sale", sale);
		return obj;
	}

	@Override
	public void decorateMapping(ObjectNode mappings) {
		mappings.putObject("sale").put("type", "long");
	}
}
