package services.product;

import java.util.List;

import dto.product.AttachmentDesc;

public interface IAttachmentDescService {
	public boolean addAttachmentDesc(AttachmentDesc attachmentDescript);

	public boolean deleteAttachmentDescByIid(Integer iid);
	
	public boolean deleteAttachmentDescByIattachmentid(Integer iid);

	public List<AttachmentDesc> getAttachmentDescriptBySearch(
			AttachmentDesc attachmentDesc);

	public List<AttachmentDesc> getAttachmentDescriptsByAttachmentId(Integer iid);

	public boolean updateAttachmentDesc(AttachmentDesc attachmentDescript);
	
	public List<AttachmentDesc> getAttachmentDescriptsByIids(List<Integer> iid);
}
