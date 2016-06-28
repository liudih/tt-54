package services.product;

import java.util.List;

import dto.product.Attachment;
import forms.product.AttachmentSearchForm;

public interface IAttachmentService {
	public boolean addAttachment(Attachment attachment);

	public boolean deleteAttachmentByIid(Integer iid);

	List<Attachment> getAttachmentBySearch(
			AttachmentSearchForm attachmentSearchForm);

	public Integer getCountBySearch(AttachmentSearchForm attachmentSearchForm);

	public Integer getMapperCountByAttachmentIid(Integer iid);

	public Attachment getAttachmentByIid(Integer iid);
}
