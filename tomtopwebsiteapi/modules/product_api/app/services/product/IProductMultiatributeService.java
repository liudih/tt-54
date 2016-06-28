package services.product;

import java.util.List;
import java.util.Map;

import context.WebContext;
import dto.product.ProductMultiattributeEntity;

public interface IProductMultiatributeService {

	Map<String, List<ProductMultiattributeEntity>> getProductMultiatribute(
			String clistingid, String mainclistingid, String type);

	Map<String, List<ProductMultiattributeEntity>> getProductMultiatribute(
			String clistingid, String mainclistingid, String type,
			WebContext context);

}
