package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductVideo;

public interface IProductVideoEnquiryDao extends IProductEnquiryDao {

	public List<ProductVideo> getVideosBylistId(String clistingid);

	public List<ProductVideo> getVideoBylistingIds(List<String> listingIds);
}
