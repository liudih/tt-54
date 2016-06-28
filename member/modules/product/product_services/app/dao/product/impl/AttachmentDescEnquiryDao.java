package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.AttachmentDescMapper;
import dao.product.IAttachmentDescEnquiryDao;
import dto.product.AttachmentDesc;

public class AttachmentDescEnquiryDao implements IAttachmentDescEnquiryDao {
	@Inject
	AttachmentDescMapper mapper;

	@Override
	public List<AttachmentDesc> getAttachmentDescriptBySearch(
			AttachmentDesc attachmentDesc) {
		String title = attachmentDesc.getCtitle();
		Integer ilanguage = attachmentDesc.getIlanguage();
		Integer iid = attachmentDesc.getIid();
		return mapper.getAttachmentDescriptBySearch(title, ilanguage, iid);
	}

	@Override
	public List<AttachmentDesc> getAttachmentDescriptsByAttachmentId(Integer iid) {
		return mapper.getAttachmentDescriptsByAttachmentId(iid);
	}

	@Override
	public List<AttachmentDesc> getAttachmentDescriptsByIids(List<Integer> iids) {
		return mapper.getAttachmentDescriptsByIids(iids);
	}

}
