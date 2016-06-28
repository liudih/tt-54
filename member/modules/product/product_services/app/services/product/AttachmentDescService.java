package services.product;

import java.util.List;

import com.google.inject.Inject;

import dao.product.IAttachmentDescEnquiryDao;
import dao.product.IAttachmentDescUpdateDao;
import dto.product.AttachmentDesc;

public class AttachmentDescService implements IAttachmentDescService {
	@Inject
	IAttachmentDescEnquiryDao enquiryDao;

	@Inject
	IAttachmentDescUpdateDao updateDao;

	@Override
	public boolean addAttachmentDesc(AttachmentDesc attachmentDescript) {
		return updateDao.addAttachmentDesc(attachmentDescript);
	}

	@Override
	public boolean deleteAttachmentDescByIid(Integer iid) {
		return updateDao.deleteAttachmentDescByIid(iid);
	}

	@Override
	public List<AttachmentDesc> getAttachmentDescriptBySearch(
			AttachmentDesc attachmentDesc) {
		return enquiryDao.getAttachmentDescriptBySearch(attachmentDesc);
	}

	@Override
	public List<AttachmentDesc> getAttachmentDescriptsByAttachmentId(Integer iid) {
		return enquiryDao.getAttachmentDescriptsByAttachmentId(iid);
	}

	@Override
	public boolean updateAttachmentDesc(AttachmentDesc attachmentDescript) {
		return updateDao.updateAttachmentDesc(attachmentDescript);
	}

	@Override
	public List<AttachmentDesc> getAttachmentDescriptsByIids(List<Integer> iids) {
		return enquiryDao.getAttachmentDescriptsByIids(iids);
	}

	@Override
	public boolean deleteAttachmentDescByIattachmentid(Integer achmentid) {
		return updateDao.deleteAttachmentDescByIattachmentid(achmentid);
	}

}
