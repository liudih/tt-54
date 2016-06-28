package services.product;

import java.util.List;

import com.google.inject.Inject;

import dao.product.IAttachmentEnquiryDao;
import dao.product.IAttachmentUpdateDao;
import dto.product.Attachment;
import forms.product.AttachmentSearchForm;

public class AttachmentService implements IAttachmentService {
	@Inject
	IAttachmentEnquiryDao enquiryDao;
	@Inject
	IAttachmentUpdateDao updateDao;

	@Override
	public boolean addAttachment(Attachment attachment) {
		boolean flag = enquiryDao.vaildateAttachmentByTypeAndPath(
				attachment.getCtype(), attachment.getCpath());
		if (flag) {
			return true;
		}
		return updateDao.addAttachment(attachment);
	}

	@Override
	public boolean deleteAttachmentByIid(Integer iid) {
		return updateDao.deleteAttachmentByIid(iid);
	}

	@Override
	public List<Attachment> getAttachmentBySearch(
			AttachmentSearchForm attachmentSearchForm) {
		return enquiryDao.getAttachmentByFilenameAndType(attachmentSearchForm);
	}

	@Override
	public Integer getCountBySearch(AttachmentSearchForm AttachmentSearchForm) {
		return enquiryDao.getCountBySearch(AttachmentSearchForm);
	}

	@Override
	public Integer getMapperCountByAttachmentIid(Integer iid) {
		return enquiryDao.getMapperCountByAttachmentIid(iid);
	}

	@Override
	public Attachment getAttachmentByIid(Integer iid) {
		return enquiryDao.getAttachmentByIid(iid);
	}

}
