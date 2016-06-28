package dao.product;

import dao.IProductUpdateDao;
import dto.product.AttachmentDesc;

public interface IAttachmentDescUpdateDao extends IProductUpdateDao {
	boolean addAttachmentDesc(AttachmentDesc addtAttachmentDescript);

	boolean deleteAttachmentDescByIid(Integer iid);
	
	boolean updateAttachmentDesc(AttachmentDesc attachmentDescript);

	boolean deleteAttachmentDescByIattachmentid(Integer achmentid);
}
