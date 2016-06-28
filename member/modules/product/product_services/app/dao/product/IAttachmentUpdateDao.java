package dao.product;

import dao.IProductUpdateDao;
import dto.product.Attachment;

public interface IAttachmentUpdateDao extends IProductUpdateDao {
	public boolean addAttachment(Attachment attachment);

	public boolean deleteAttachmentByIid(Integer iid);
}
