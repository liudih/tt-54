package dao.product.impl;

import javax.inject.Inject;

import mapper.product.AttachmentMapper;
import dao.product.IAttachmentUpdateDao;
import dto.product.Attachment;

public class AttachmentUpdateDao implements IAttachmentUpdateDao {
	@Inject
	AttachmentMapper mapper;

	@Override
	public boolean addAttachment(Attachment attachment) {
		return mapper.addAttachment(attachment.getCdescribe(),
				attachment.getCtype(), attachment.getCfilename(),
				attachment.getCpath(), attachment.getCcreateuser(),
				attachment.getDcreatedate()) > 0 ? true : false;
	}

	@Override
	public boolean deleteAttachmentByIid(Integer iid) {
		return mapper.deleteAttachmentByIid(iid) > 0 ? true : false;
	}

}
