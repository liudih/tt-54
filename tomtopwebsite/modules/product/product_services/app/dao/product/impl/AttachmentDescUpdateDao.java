package dao.product.impl;

import javax.inject.Inject;

import mapper.product.AttachmentDescMapper;
import dao.product.IAttachmentDescUpdateDao;
import dto.product.AttachmentDesc;

public class AttachmentDescUpdateDao implements IAttachmentDescUpdateDao {
	@Inject
	AttachmentDescMapper mapper;

	@Override
	public boolean addAttachmentDesc(AttachmentDesc addtAttachmentDescript) {
		return mapper.addAttachmentDesc(addtAttachmentDescript.getIlanguage(),
				addtAttachmentDescript.getCtitle(),
				addtAttachmentDescript.getCdescribe(),
				addtAttachmentDescript.getIattachmentid(),
				addtAttachmentDescript.getCcreateuser(),
				addtAttachmentDescript.getDcreatedate()) > 0 ? true : false;
	}

	@Override
	public boolean deleteAttachmentDescByIid(Integer iid) {
		return mapper.deleteAttachmentDescByIid(iid) > 0 ? true : false;
	}

	@Override
	public boolean updateAttachmentDesc(AttachmentDesc attachmentDescript) {
		return mapper.updateAttachmentDesc(attachmentDescript.getCtitle(),
				attachmentDescript.getCdescribe(),
				attachmentDescript.getCupdateuser(),
				attachmentDescript.getDupdatedate(),
				attachmentDescript.getIid()) > 0 ? true : false;
	}

	@Override
	public boolean deleteAttachmentDescByIattachmentid(Integer achmentid) {
		return mapper.deleteAttachmentDescByAttachmentid(achmentid) > 0 ? true
				: false;
	}

}
