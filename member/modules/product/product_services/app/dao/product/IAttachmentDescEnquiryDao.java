package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.AttachmentDesc;

public interface IAttachmentDescEnquiryDao extends IProductEnquiryDao {
	List<AttachmentDesc> getAttachmentDescriptBySearch(
			AttachmentDesc attachmentDesc);

	List<AttachmentDesc> getAttachmentDescriptsByAttachmentId(Integer iid);

	List<AttachmentDesc> getAttachmentDescriptsByIids(List<Integer> iids);
}
