package services.product;

import java.util.List;

import dto.product.ProductExplain;

public interface IProductExplainService {

	public abstract List<ProductExplain> getProductExplainsBySiteAndLan(
			Integer websiteId, Integer languageId);

	public abstract boolean updateProductExplain(ProductExplain productExplain);

	public abstract boolean addProductExplain(ProductExplain productExplain);

	public abstract boolean deleteProductExplainById(Integer iid);

	public abstract ProductExplain getProductExplainBySiteIdAndLanIdAndType(
			Integer websiteId, Integer languageId, String type);

}