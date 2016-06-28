package dao.product;

import dao.IProductUpdateDao;
import dto.product.ProductAttachmentMapper;

public interface IProductAttachmentMapperUpdateDao extends IProductUpdateDao {
	public boolean addProductAttachmentMapper(
			ProductAttachmentMapper productAttachmentMapper);

	public boolean deleteProductAttachmentMapperByIid(Integer iid);
}
