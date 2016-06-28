package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.AttachmentMapper;
import dao.product.IAttachmentEnquiryDao;
import dto.product.Attachment;
import forms.product.AttachmentSearchForm;

public class AttachmentEnquiryDao implements IAttachmentEnquiryDao {
	@Inject
	AttachmentMapper mapper;

	@Override
	public Integer getCountBySearch(AttachmentSearchForm attachmentSearchForm) {
		return mapper.getCountBySearch(attachmentSearchForm.getCfilename(),
				attachmentSearchForm.getCtype(),
				attachmentSearchForm.getCpath());
	}

	@Override
	public Integer getMapperCountByAttachmentIid(Integer iid) {
		return mapper.getMapperCountByAttachmentIid(iid);
	}

	@Override
	public List<Attachment> getAttachmentByFilenameAndType(
			AttachmentSearchForm attachmentSearchForm) {
		return mapper.getAttachmentByFilenameAndType(
				attachmentSearchForm.getCfilename(),
				attachmentSearchForm.getCtype(),
				attachmentSearchForm.getCpath(),
				attachmentSearchForm.getPageSize(),
				attachmentSearchForm.getPageNum());
	}

	@Override
	public boolean vaildateAttachmentByTypeAndPath(String ctype, String cpath) {
		return mapper.vaildateAttachmentByTypeAndPath(ctype, cpath) > 0 ? true
				: false;
	}

	@Override
	public Attachment getAttachmentByIid(Integer iid) {
		return mapper.getAttachmentByIid(iid);
	}

}
