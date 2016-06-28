package services.product;

import java.util.List;

import javax.inject.Inject;

import dao.product.IProductExplainEnquiryDao;
import dao.product.IProductExplainUpdateDao;
import dto.product.ProductExplain;

public class ProductExplainService implements IProductExplainService {
	@Inject
	IProductExplainEnquiryDao productExplainEnquityDao;

	@Inject
	IProductExplainUpdateDao productExplainUpdateDao;

	/* (non-Javadoc)
	 * @see services.product.IProductExplainService#getProductExplainsBySiteAndLan(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<ProductExplain> getProductExplainsBySiteAndLan(
			Integer websiteId, Integer languageId) {
		return productExplainEnquityDao.getProductExplainsBySiteAndLan(
				websiteId, languageId);
	}

	/* (non-Javadoc)
	 * @see services.product.IProductExplainService#updateProductExplain(entity.product.ProductExplain)
	 */
	@Override
	public boolean updateProductExplain(ProductExplain productExplain) {
		return productExplainUpdateDao.updateProductExplain(productExplain);
	}

	/* (non-Javadoc)
	 * @see services.product.IProductExplainService#addProductExplain(entity.product.ProductExplain)
	 */
	@Override
	public boolean addProductExplain(ProductExplain productExplain) {
		return productExplainUpdateDao.addProductExplain(productExplain);
	}

	/* (non-Javadoc)
	 * @see services.product.IProductExplainService#deleteProductExplainById(java.lang.Integer)
	 */
	@Override
	public boolean deleteProductExplainById(Integer iid) {
		return productExplainUpdateDao.deleteByIid(iid);
	}

	/* (non-Javadoc)
	 * @see services.product.IProductExplainService#getProductExplainBySiteIdAndLanIdAndType(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ProductExplain getProductExplainBySiteIdAndLanIdAndType(
			Integer websiteId, Integer languageId, String type) {
		return productExplainEnquityDao
				.getProductExplainBySiteIdAndLanIdAndType(websiteId,
						languageId, type);
	}
}
