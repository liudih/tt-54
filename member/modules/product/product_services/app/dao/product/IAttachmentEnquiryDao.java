package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.Attachment;
import forms.product.AttachmentSearchForm;

public interface IAttachmentEnquiryDao extends IProductEnquiryDao {
	boolean vaildateAttachmentByTypeAndPath(String ctype, String cpath);

	Integer getCountBySearch(AttachmentSearchForm attachmentSearchForm);

	Integer getMapperCountByAttachmentIid(Integer iid);

	List<Attachment> getAttachmentByFilenameAndType(
			AttachmentSearchForm attachmentSearchForm);

	Attachment getAttachmentByIid(Integer iid);
}
