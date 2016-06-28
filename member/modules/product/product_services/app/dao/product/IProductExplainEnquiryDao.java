package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductExplain;

public interface IProductExplainEnquiryDao extends IProductEnquiryDao {
	public String getContentForSiteAndLanAndType(int site, int lan, String type);

	public List<ProductExplain> getProductExplainsBySiteAndLan(int site, int lan);

	public ProductExplain getProductExplainBySiteIdAndLanIdAndType(int site,
			int lan, String type);
}
